package com.ship.trackly.service;

import com.ship.trackly.dto.ShipmentDTO;
import com.ship.trackly.model.Shipment;
import com.ship.trackly.repository.ShipmentRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ShipmentService {

    private final ShipmentRepository shipmentRepository;

    public Shipment createShipment(ShipmentDTO.ShipmentRequest request) {
        Shipment shipment = mapToShipment(request);
        return shipmentRepository.save(shipment);
    }

    private Shipment mapToShipment(ShipmentDTO.ShipmentRequest request) {
        return Shipment.builder()
                .trackingNumber(generateTrackingNumber())
                .origin(request.getOrigin())
                .destination(request.getDestination())
                .estimatedDelivery(request.getEstimatedDelivery())
                .build();
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
        return shipmentRepository.save(shipment);
    }

    private String generateTrackingNumber() {
        return "TRK-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

}
