package com.hogatte.bmtc.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class VehicleTripDetailsResponse {

    @JsonProperty("RouteDetails")
    private List<RouteDetail> RouteDetails;

    @JsonProperty("LiveLocation")
    private List<LiveLocationEntry> LiveLocation;

    @JsonProperty("Message")
    private String Message;

    @JsonProperty("RowCount")
    private Integer RowCount;

    @JsonProperty("trip_status")
    @JsonAlias({"tripstatus"})
    private String tripStatus;

    @JsonProperty("Issuccess")
    private boolean Issuccess;

    @JsonProperty("currlatitude")
    private Double currlatitude;

    @JsonProperty("currlongitude")
    private Double currlongitude;


    @Data
    public static class RouteDetail {
        @JsonProperty("routeid")
        private Long routeid;
        @JsonProperty("vehicleid")
        private Long vehicleid;
        @JsonProperty("stationid")
        private Long stationid;
        @JsonProperty("stationname")
        private String stationname;
        @JsonProperty("sourcestation")
        private String sourcestation;
        @JsonProperty("destinationstation")
        private String destinationstation;
        @JsonProperty("latitude")
        private Double latitude;
        @JsonProperty("longitude")
        private Double longitude;
        @JsonProperty("tripposition")
        private Integer tripposition;

    }

    @Data
    public static class LiveLocationEntry {
        @JsonProperty("latitude")
        private Double latitude;
        @JsonProperty("longitude")
        private Double longitude;
        @JsonProperty("vehicleid")
        private Long vehicleid;
        @JsonProperty("vehiclenumber")
        private String vehiclenumber;

    }
}