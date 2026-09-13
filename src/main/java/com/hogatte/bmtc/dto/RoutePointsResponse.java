package com.hogatte.bmtc.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class RoutePointsResponse {
    @JsonProperty("data")
    private List<RoutePointEntry> data;

    @JsonProperty("Message")
    private String Message;

    @JsonProperty("Issuccess")
    private boolean Issuccess;


    @Data
    public static class RoutePointEntry {
        @JsonProperty("latitude")
        private String latitude;
        @JsonProperty("longitude")
        private String longitude;

    }
}