package com.ship.trackly.controller;

import com.ship.trackly.dto.ShipmentDTO;
import com.ship.trackly.model.Shipment;
import com.ship.trackly.service.ShipmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/shipments")
@RequiredArgsConstructor
public class ShipmentController {

    private final ShipmentService shipmentService;

    @PostMapping()
    public ResponseEntity<Shipment> createShipment(
            @Valid @RequestBody
            ShipmentDTO.ShipmentRequest request
    ) {
        return ResponseEntity.ok().body(shipmentService.createShipment(request));
    }

    @GetMapping
    public ResponseEntity<List<Shipment>> getAllShipments() {
        return ResponseEntity.ok().body(shipmentService.getAllShipments());
    }

    @GetMapping({"/{id}"})
    public ResponseEntity<Shipment> getShipmentById(@PathVariable String id) {
        return ResponseEntity.ok().body(shipmentService.getShipmentById(id));
    }

    @GetMapping({"/tracking/{trackingNumber}"})
    public ResponseEntity<Shipment> getShipmentByTrackingNumber(@PathVariable String trackingNumber) {
        return ResponseEntity.ok().body(shipmentService.getShipmentByTrackingNumber(trackingNumber));
    }

    @PutMapping({"/{id}/status"})
    public ResponseEntity<Shipment> updateShipmentStatus(
            @PathVariable String id,
            @Valid @RequestBody
            ShipmentDTO.UpdateShipmentStatusRequest status
    ) {
        return ResponseEntity.ok().body(shipmentService.updateShipmentStatus(id, status));
    }

}
