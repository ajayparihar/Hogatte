package com.hogatte.feature.destinationcheck.impl;

import com.hogatte.bmtc.client.BmtcApiClient;
import com.hogatte.bmtc.dto.VehicleTripDetailsResponse;
import com.hogatte.feature.destinationcheck.DestinationCheckService;
import com.hogatte.feature.destinationcheck.model.DestinationCheckRequest;
import com.hogatte.feature.destinationcheck.model.DestinationCheckResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DestinationCheckServiceImpl implements DestinationCheckService {

    private final BmtcApiClient bmtcApiClient;

    @Override
    public DestinationCheckResponse check(DestinationCheckRequest request) {
        Long vehicleId = request.getVehicleid();
        String targetStationName = request.getStationName() != null ? request.getStationName().trim() : "";

        // Step 1: Call /api/v1/vehicles/trip-details
        VehicleTripDetailsResponse tripDetails = bmtcApiClient.getVehicleTripDetails(vehicleId);

        if (tripDetails == null || tripDetails.getRouteDetails() == null || tripDetails.getRouteDetails().isEmpty()
                || isTripNotInProgress(tripDetails)) {
            return new DestinationCheckResponse(
                    vehicleId,
                    null,
                    null,
                    targetStationName,
                    "NO_TRIP",
                    false,
                    "No trip is currently in progress for this vehicle");
        }

        String vehicleNumber = null;
        if (tripDetails.getLiveLocation() != null && !tripDetails.getLiveLocation().isEmpty()) {
            vehicleNumber = tripDetails.getLiveLocation().get(0).getVehiclenumber();
        }

        // Step 2: Get RouteDetails
        List<VehicleTripDetailsResponse.RouteDetail> routeDetails = tripDetails.getRouteDetails();

        String normalizedTarget = normalize(targetStationName);

        // Step 3: Find target station by name
        int targetStationIndex = -1;
        VehicleTripDetailsResponse.RouteDetail targetStation = null;

        // 1st attempt: exact match ignoring cases and spaces
        for (int i = 0; i < routeDetails.size(); i++) {
            VehicleTripDetailsResponse.RouteDetail stop = routeDetails.get(i);
            if (stop.getStationname() != null && normalize(stop.getStationname()).equals(normalizedTarget)) {
                targetStationIndex = i;
                targetStation = stop;
                break;
            }
        }

        // 2nd attempt: contains match ignoring cases and spaces
        if (targetStationIndex == -1 && !normalizedTarget.isEmpty()) {
            for (int i = 0; i < routeDetails.size(); i++) {
                VehicleTripDetailsResponse.RouteDetail stop = routeDetails.get(i);
                if (stop.getStationname() != null && normalize(stop.getStationname()).contains(normalizedTarget)) {
                    targetStationIndex = i;
                    targetStation = stop;
                    break;
                }
            }
        }

        // Step 4: If NOT FOUND
        if (targetStationIndex == -1) {
            return new DestinationCheckResponse(
                    vehicleId,
                    vehicleNumber,
                    null,
                    targetStationName,
                    "NOT_FOUND",
                    false,
                    "Station is not on this vehicle's current route");
        }

        Long stationId = targetStation.getStationid();
        String matchedStationName = targetStation.getStationname();

        // Step 5: Determine vehicle's current position relative to station order
        Double liveLat = null;
        Double liveLon = null;
        if (tripDetails.getLiveLocation() != null && !tripDetails.getLiveLocation().isEmpty()) {
            VehicleTripDetailsResponse.LiveLocationEntry live = tripDetails.getLiveLocation().get(0);
            liveLat = live.getLatitude();
            liveLon = live.getLongitude();
        } else if (tripDetails.getCurrlatitude() != null && tripDetails.getCurrlongitude() != null) {
            liveLat = tripDetails.getCurrlatitude();
            liveLon = tripDetails.getCurrlongitude();
        }

        int vehicleCurrentPositionIndex = -1;
        if (liveLat != null && liveLon != null) {
            vehicleCurrentPositionIndex = findNearestStationIndex(routeDetails, liveLat, liveLon);
        } else {
            for (int i = 0; i < routeDetails.size(); i++) {
                if (Integer.valueOf(2).equals(routeDetails.get(i).getTripposition())) {
                    vehicleCurrentPositionIndex = i;
                    break;
                }
            }
        }

        if (vehicleCurrentPositionIndex == -1) {
            return new DestinationCheckResponse(
                    vehicleId,
                    vehicleNumber,
                    stationId,
                    matchedStationName,
                    "LOCATION_UNAVAILABLE",
                    false,
                    "Live location unavailable for this vehicle");
        }

        // Step 6: Compare target station order with vehicle's current position
        if (targetStationIndex >= vehicleCurrentPositionIndex) {
            return new DestinationCheckResponse(
                    vehicleId,
                    vehicleNumber,
                    stationId,
                    matchedStationName,
                    "AHEAD",
                    true,
                    "Bus is heading towards your destination");
        } else {
            return new DestinationCheckResponse(
                    vehicleId,
                    vehicleNumber,
                    stationId,
                    matchedStationName,
                    "PASSED",
                    false,
                    "Bus has already passed your destination");
        }
    }

    private int findNearestStationIndex(List<VehicleTripDetailsResponse.RouteDetail> routeDetails, double lat,
            double lon) {
        int nearestIndex = 0;
        double minDistance = Double.MAX_VALUE;
        for (int i = 0; i < routeDetails.size(); i++) {
            VehicleTripDetailsResponse.RouteDetail stop = routeDetails.get(i);
            if (stop.getLatitude() != null && stop.getLongitude() != null) {
                double dist = distanceBetween(lat, lon, stop.getLatitude(), stop.getLongitude());
                if (dist < minDistance) {
                    minDistance = dist;
                    nearestIndex = i;
                }
            }
        }
        return nearestIndex;
    }

    private double distanceBetween(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                        * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return 6371000 * c;
    }

    private boolean isTripNotInProgress(VehicleTripDetailsResponse tripDetails) {
        if ("No Records Found".equalsIgnoreCase(tripDetails.getMessage())) {
            return true;
        }
        if (tripDetails.getRowCount() != null && tripDetails.getRowCount() <= 1
                && tripDetails.getRouteDetails() == null) {
            return true;
        }
        return false;
    }

    private String normalize(String name) {
        if (name == null) {
            return "";
        }
        return name.replaceAll("\\s+", "").toLowerCase();
    }
}