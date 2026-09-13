package com.hogatte.feature.vehiclelist.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VehicleListRequest {
    @NotBlank(message = "vehicleRegNo must not be blank")
    private String vehicleRegNo;
}
