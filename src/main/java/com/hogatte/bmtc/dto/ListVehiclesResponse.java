package com.hogatte.bmtc.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class ListVehiclesResponse {
    @JsonProperty("data")
    private List<Vehicle> data;

    @JsonProperty("Message")
    private String Message;

    @JsonProperty("Issuccess")
    private boolean Issuccess;


    @Data
    public static class Vehicle {
        @JsonProperty("vehicleid")
        private Long vehicleid;
        @JsonProperty("vehicleregno")
        private String vehicleregno;
        @JsonProperty("responsecode")
        private Integer responsecode;

    }
}