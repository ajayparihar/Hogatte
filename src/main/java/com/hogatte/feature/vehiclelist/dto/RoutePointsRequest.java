package com.hogatte.feature.vehiclelist.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoutePointsRequest {
    @NotNull(message = "routeId must not be null")
    private Long routeId;
}
