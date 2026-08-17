package com.ship.trackly.repository;


import com.ship.trackly.model.Shipment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ShipmentRepository extends JpaRepository<Shipment, String> {

    Optional<Shipment> findByTrackingNumber(String trackingNumber);
}
