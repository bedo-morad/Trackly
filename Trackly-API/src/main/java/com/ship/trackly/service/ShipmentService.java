package com.ship.trackly.service;

import com.ship.trackly.dto.ShipmentDTO;
import com.ship.trackly.model.Shipment;
import com.ship.trackly.model.ShipmentStatus;
import com.ship.trackly.repository.ShipmentRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ShipmentService {

    private final ShipmentRepository shipmentRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public Shipment createShipment(ShipmentDTO.ShipmentRequest request) {
        Shipment shipment = mapToShipment(request);
        shipment =  shipmentRepository.save(shipment);
        notifyShipmentStatusUpdate(shipment, getStatusMessage(shipment.getStatus()));
        return shipment;
    }

    public List<Shipment> getAllShipments() {
        return shipmentRepository.findAll();
    }

    public Shipment getShipmentById(String id) {
        return shipmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Shipment not found"));
    }


    public Shipment getShipmentByTrackingNumber(String trackingNumber) {
        return shipmentRepository.findByTrackingNumber(trackingNumber)
                .orElseThrow(() -> new EntityNotFoundException("Shipment not found"));
    }

    public Shipment updateShipmentStatus(String id, ShipmentDTO.UpdateShipmentStatusRequest status) {
        Shipment shipment = getShipmentById(id);
        shipment.setStatus(status.getStatus());
        if (status.getCurrentLocation() != null) {
            shipment.setCurrentLocation(status.getCurrentLocation());
        }
        shipment =  shipmentRepository.save(shipment);
        notifyShipmentStatusUpdate(shipment, getStatusMessage(shipment.getStatus()));
        return shipment;
    }


    public void notifyShipmentStatusUpdate(Shipment shipment, String message) {
        ShipmentDTO.StatusUpdateMessage update
                = ShipmentDTO.StatusUpdateMessage.builder()
                .shipmentId(shipment.getId())
                .trackingNumber(shipment.getTrackingNumber())
                .status(shipment.getStatus())
                .currentLocation(shipment.getCurrentLocation())
                .timestamp(shipment.getUpdatedAt())
                .message(message)
                .build();

        messagingTemplate.convertAndSend(
                "/topic/shipment-status" + shipment.getId(),
                update
        );
        messagingTemplate.convertAndSend(
                "/topic/shipment-status",
                update
        );

        log.info("Notified shipment status update for shipment ID: {}", shipment.getId());
    }

    private Shipment mapToShipment(ShipmentDTO.ShipmentRequest request) {
        return Shipment.builder()
                .trackingNumber(generateTrackingNumber())
                .origin(request.getOrigin())
                .destination(request.getDestination())
                .estimatedDelivery(request.getEstimatedDelivery())
                .build();
    }

    private String generateTrackingNumber() {
        return "TRK-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private String getStatusMessage(ShipmentStatus status) {
        return switch (status) {
            case ORDER_PLACED -> "Your Order has been placed.";
            case PROCESSING -> "Your Order is being processed.";
            case PICKED_UP -> "Your Order has been picked up.";
            case IN_TRANSIT -> "Your Order is in transit.";
            case OUT_FOR_DELIVERY -> "Your Order is out for delivery.";
            case DELIVERED -> "Your Order has been delivered.";
            case EXCEPTION -> "There is an exception with your Order.";
        };
    }
}
