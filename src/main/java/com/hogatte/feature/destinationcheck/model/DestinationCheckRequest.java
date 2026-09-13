package com.hogatte.feature.destinationcheck.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DestinationCheckRequest {

    @NotNull(message = "vehicleid is required")
    @JsonProperty("vehicleid")
    @JsonAlias({"vehicleId", "vehicle_id"})
    private Long vehicleid;

    @NotBlank(message = "stationName is required")
    @JsonProperty("stationName")
    @JsonAlias({"stationname", "station_name", "StopName", "stopName", "stop_name"})
    private String stationName;

    @JsonProperty("stationid")
    @JsonAlias({"stationId", "station_id"})
    private Long stationid;

    public DestinationCheckRequest(Long vehicleid, String stationName) {
        this.vehicleid = vehicleid;
        this.stationName = stationName;
    }
}