package com.hogatte.feature.vehiclelist.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BusStopRequest {
    @NotBlank(message = "stationName must not be blank")
    private String stationName;
}
