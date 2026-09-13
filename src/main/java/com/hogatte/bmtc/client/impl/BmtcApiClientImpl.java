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
        try {
            String jsonToParse = response;
            if (response.startsWith("\"") && response.endsWith("\"")) {
                jsonToParse = response.substring(1, response.length() - 1);
                jsonToParse = jsonToParse.replace("\\\"", "\"");
            }
            return objectMapper.readValue(jsonToParse, ListVehiclesResponse.class);
        } catch (Exception e) {
            log.error("Failed to parse vehicle list response for vehicleRegNo: {}", vehicleRegNo, e);
            throw new RuntimeException("Failed to parse vehicle list", e);
        }
    }

    @Override
    public VehicleTripDetailsResponse getVehicleTripDetails(Long vehicleId) {
        String response = vehicleListService.fetchVehicleTripDetails(vehicleId);
        try {
            // Handle case where response is wrapped in quotes (JSON string)
            String jsonToParse = response;
            if (response.startsWith("\"") && response.endsWith("\"")) {
                jsonToParse = response.substring(1, response.length() - 1);
                // Unescape escaped quotes
                jsonToParse = jsonToParse.replace("\\\"", "\"");
            }
            return objectMapper.readValue(jsonToParse, VehicleTripDetailsResponse.class);
        } catch (Exception e) {
            log.error("Failed to parse vehicle trip details for vehicleId: {}", vehicleId, e);
            throw new RuntimeException("Failed to parse vehicle trip details: " + e.getMessage(), e);
        }
    }

    @Override
    public RoutePointsResponse getRoutePoints(Long routeId) {
        String response = vehicleListService.fetchRoutePoints(routeId);
        try {
            String jsonToParse = response;
            if (response.startsWith("\"") && response.endsWith("\"")) {
                jsonToParse = response.substring(1, response.length() - 1);
                jsonToParse = jsonToParse.replace("\\\"", "\"");
            }
            return objectMapper.readValue(jsonToParse, RoutePointsResponse.class);
        } catch (Exception e) {
            log.error("Failed to parse route points for routeId: {}", routeId, e);
            throw new RuntimeException("Failed to parse route points", e);
        }
    }

    @Override
    public BusStopResponse findBusStops(String stationName) {
        String response = vehicleListService.fetchBusStops(stationName);
        try {
            String jsonToParse = response;
            if (response.startsWith("\"") && response.endsWith("\"")) {
                jsonToParse = response.substring(1, response.length() - 1);
                jsonToParse = jsonToParse.replace("\\\"", "\"");
            }
            return objectMapper.readValue(jsonToParse, BusStopResponse.class);
        } catch (Exception e) {
            log.error("Failed to parse bus stops for stationName: {}", stationName, e);
            throw new RuntimeException("Failed to parse bus stops", e);
        }
    }
}