package com.hogatte.bmtc.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class BusStopResponse {
    private List<BusStop> data;

    @JsonProperty("Message")
    private String Message;

    @JsonProperty("Issuccess")
    private boolean Issuccess;

    private String exception;

    @JsonProperty("RowCount")
    private Integer RowCount;

    private Integer responsecode;

    @Data
    public static class BusStop {
        private Integer srno;
        private String routeno;
        private Long routeid;
        private Double center_lat;
        private Double center_lon;
        private Integer responsecode;
        private String routetypeid;
        private String routename;
        private String route;
    }
}
