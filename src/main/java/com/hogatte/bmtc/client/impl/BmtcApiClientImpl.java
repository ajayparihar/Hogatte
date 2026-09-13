package com.hogatte.bmtc.client.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hogatte.bmtc.client.BmtcApiClient;
import com.hogatte.bmtc.dto.BusStopResponse;
import com.hogatte.bmtc.dto.ListVehiclesResponse;
import com.hogatte.bmtc.dto.RoutePointsResponse;
import com.hogatte.bmtc.dto.VehicleTripDetailsResponse;
import com.hogatte.feature.vehiclelist.VehicleListService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class BmtcApiClientImpl implements BmtcApiClient {

    private final VehicleListService vehicleListService;
    private final ObjectMapper objectMapper;

    @Override
    public ListVehiclesResponse listVehicles(String vehicleRegNo) {
        String response = vehicleListService.fetchVehicles(vehicleRegNo);
        return parseResponse(response, ListVehiclesResponse.class, "list vehicles response", "vehicleRegNo=" + vehicleRegNo);
    }

    @Override
    public VehicleTripDetailsResponse getVehicleTripDetails(Long vehicleId) {
        String response = vehicleListService.fetchVehicleTripDetails(vehicleId);
        return parseResponse(response, VehicleTripDetailsResponse.class, "vehicle trip details response", "vehicleId=" + vehicleId);
    }

    @Override
    public RoutePointsResponse getRoutePoints(Long routeId) {
        String response = vehicleListService.fetchRoutePoints(routeId);
        return parseResponse(response, RoutePointsResponse.class, "route points response", "routeId=" + routeId);
    }

    @Override
    public BusStopResponse findBusStops(String stationName) {
        String response = vehicleListService.fetchBusStops(stationName);
        return parseResponse(response, BusStopResponse.class, "bus stop response", "stationName=" + stationName);
    }

    private <T> T parseResponse(String response, Class<T> responseType, String operation, String identifier) {
        try {
            String json = response == null ? null : response.trim();
            if (json != null && json.startsWith("\"") && json.endsWith("\"")) {
                json = objectMapper.readValue(json, String.class);
            }
            return objectMapper.readValue(json, responseType);
        } catch (Exception e) {
            log.error("Failed to parse {} for {}", operation, identifier, e);
            throw new RuntimeException("Failed to parse " + operation + ": " + e.getMessage(), e);
        }
    }
}