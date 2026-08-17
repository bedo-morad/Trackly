package com.ship.trackly.dto;

import com.ship.trackly.model.ShipmentStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

public class ShipmentDTO {

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ShipmentRequest {
        @NotNull(message = "Origin is required")
        private String origin;
        @NotBlank(message = "Destination is required")
        private String destination;
        private String estimatedDelivery;
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UpdateShipmentStatusRequest {
        private ShipmentStatus status;
        private String currentLocation;
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class StatusUpdateMessage{
        private String shipmentId;
        private String trackingNumber;
        private ShipmentStatus status;
        private String currentLocation;
        private LocalDateTime timestamp;
        private String message;
    }
}