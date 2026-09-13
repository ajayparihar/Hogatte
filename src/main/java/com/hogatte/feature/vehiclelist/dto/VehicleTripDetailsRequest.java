package com.hogatte.feature.vehiclelist.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VehicleTripDetailsRequest {
    @NotNull(message = "vehicleId must not be null")
    private Long vehicleId;
}
