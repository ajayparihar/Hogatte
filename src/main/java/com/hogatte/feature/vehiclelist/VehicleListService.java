package com.hogatte.feature.vehiclelist;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class VehicleListService {

    private final RestClient bmtcRestClient;
    private static final String USER_AGENT = "Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/139.0.0.0 Mobile Safari/537.36";

    public String fetchVehicles(String vehicleRegNo) {
        return bmtcRestClient.post()
                .uri("/ListVehicles")
                .header("User-Agent", USER_AGENT)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Map.of("vehicleRegNo", vehicleRegNo))
                .retrieve()
                .body(String.class);
    }

    public String fetchVehicleTripDetails(Long vehicleId) {
        return bmtcRestClient.post()
                .uri("/VehicleTripDetails_v2")
                .header("User-Agent", USER_AGENT)
                .header("deviceType", "WEB")
                .header("lan", "en")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Map.of("vehicleId", vehicleId))
                .retrieve()
                .body(String.class);
    }

    public String fetchRoutePoints(Long routeId) {
        return bmtcRestClient.post()
                .uri("/RoutePoints")
                .header("User-Agent", USER_AGENT)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Map.of("routeid", routeId))
                .retrieve()
                .body(String.class);
    }

    public String fetchBusStops(String stationName) {
        return bmtcRestClient.post()
                .uri("/FindNearByBusStop_v2")
                .header("User-Agent", USER_AGENT)
                .header("lan", "en")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Map.of("stationName", stationName))
                .retrieve()
                .body(String.class);
    }
}
