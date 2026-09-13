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
        String targetStationName = getTrimmedStationName(request);

        VehicleTripDetailsResponse tripDetails = bmtcApiClient.getVehicleTripDetails(vehicleId);
        if (hasNoTrip(tripDetails)) {
            return buildNoTripResponse(vehicleId, targetStationName);
        }

        String vehicleNumber = getVehicleNumber(tripDetails);
        List<VehicleTripDetailsResponse.RouteDetail> routeDetails = tripDetails.getRouteDetails();
        TargetStationMatch targetStation = findTargetStation(routeDetails, targetStationName);

        if (targetStation == null) {
            return buildNotFoundResponse(vehicleId, vehicleNumber, targetStationName);
        }

        int vehicleCurrentPositionIndex = findVehicleCurrentPositionIndex(tripDetails, routeDetails);
        if (vehicleCurrentPositionIndex == -1) {
            return buildLocationUnavailableResponse(vehicleId, vehicleNumber, targetStation.station);
        }

        return buildDestinationResponse(vehicleId, vehicleNumber, targetStation, vehicleCurrentPositionIndex);
    }

    private String getTrimmedStationName(DestinationCheckRequest request) {
        return request.getStationName() != null ? request.getStationName().trim() : "";
    }

    private boolean hasNoTrip(VehicleTripDetailsResponse tripDetails) {
        return tripDetails == null
                || tripDetails.getRouteDetails() == null
                || tripDetails.getRouteDetails().isEmpty()
                || isTripNotInProgress(tripDetails);
    }

    private String getVehicleNumber(VehicleTripDetailsResponse tripDetails) {
        if (tripDetails.getLiveLocation() == null || tripDetails.getLiveLocation().isEmpty()) {
            return null;
        }
        return tripDetails.getLiveLocation().getFirst().getVehiclenumber();
    }

    private TargetStationMatch findTargetStation(List<VehicleTripDetailsResponse.RouteDetail> routeDetails,
            String targetStationName) {
        String normalizedTarget = normalize(targetStationName);
        for (int i = 0; i < routeDetails.size(); i++) {
            VehicleTripDetailsResponse.RouteDetail stop = routeDetails.get(i);
            if (hasMatchingStationName(stop, normalizedTarget)) {
                return new TargetStationMatch(i, stop);
            }
        }

        if (normalizedTarget.isEmpty()) {
            return null;
        }

        for (int i = 0; i < routeDetails.size(); i++) {
            VehicleTripDetailsResponse.RouteDetail stop = routeDetails.get(i);
            if (stop.getStationname() != null && normalize(stop.getStationname()).contains(normalizedTarget)) {
                return new TargetStationMatch(i, stop);
            }
        }
        return null;
    }

    private boolean hasMatchingStationName(VehicleTripDetailsResponse.RouteDetail stop, String normalizedTarget) {
        return stop.getStationname() != null && normalize(stop.getStationname()).equals(normalizedTarget);
    }

    private int findVehicleCurrentPositionIndex(VehicleTripDetailsResponse tripDetails,
            List<VehicleTripDetailsResponse.RouteDetail> routeDetails) {
        Double liveLat = getLiveLatitude(tripDetails);
        Double liveLon = getLiveLongitude(tripDetails);

        if (liveLat != null && liveLon != null) {
            return findNearestStationIndex(routeDetails, liveLat, liveLon);
        }

        for (int i = 0; i < routeDetails.size(); i++) {
            if (Integer.valueOf(2).equals(routeDetails.get(i).getTripposition())) {
                return i;
            }
        }
        return -1;
    }

    private Double getLiveLatitude(VehicleTripDetailsResponse tripDetails) {
        if (tripDetails.getLiveLocation() != null && !tripDetails.getLiveLocation().isEmpty()) {
            return tripDetails.getLiveLocation().getFirst().getLatitude();
        }
        if (tripDetails.getCurrlatitude() != null && tripDetails.getCurrlongitude() != null) {
            return tripDetails.getCurrlatitude();
        }
        return null;
    }

    private Double getLiveLongitude(VehicleTripDetailsResponse tripDetails) {
        if (tripDetails.getLiveLocation() != null && !tripDetails.getLiveLocation().isEmpty()) {
            return tripDetails.getLiveLocation().getFirst().getLongitude();
        }
        if (tripDetails.getCurrlatitude() != null && tripDetails.getCurrlongitude() != null) {
            return tripDetails.getCurrlongitude();
        }
        return null;
    }

    private DestinationCheckResponse buildNoTripResponse(Long vehicleId, String targetStationName) {
        return new DestinationCheckResponse(
                vehicleId,
                null,
                null,
                targetStationName,
                "NO_TRIP",
                false,
                "No trip is currently in progress for this vehicle");
    }

    private DestinationCheckResponse buildNotFoundResponse(Long vehicleId, String vehicleNumber,
            String targetStationName) {
        return new DestinationCheckResponse(
                vehicleId,
                vehicleNumber,
                null,
                targetStationName,
                "NOT_FOUND",
                false,
                "Station is not on this vehicle's current route");
    }

    private DestinationCheckResponse buildLocationUnavailableResponse(Long vehicleId, String vehicleNumber,
            VehicleTripDetailsResponse.RouteDetail targetStation) {
        return new DestinationCheckResponse(
                vehicleId,
                vehicleNumber,
                targetStation.getStationid(),
                targetStation.getStationname(),
                "LOCATION_UNAVAILABLE",
                false,
                "Live location unavailable for this vehicle");
    }

    private DestinationCheckResponse buildDestinationResponse(Long vehicleId, String vehicleNumber,
            TargetStationMatch targetStation, int vehicleCurrentPositionIndex) {
        Long stationId = targetStation.station.getStationid();
        String matchedStationName = targetStation.station.getStationname();

        if (targetStation.index >= vehicleCurrentPositionIndex) {
            return new DestinationCheckResponse(
                    vehicleId,
                    vehicleNumber,
                    stationId,
                    matchedStationName,
                    "AHEAD",
                    true,
                    "Bus is heading towards your destination");
        }
        return new DestinationCheckResponse(
                vehicleId,
                vehicleNumber,
                stationId,
                matchedStationName,
                "PASSED",
                false,
                "Bus has already passed your destination");
    }

    private static final class TargetStationMatch {
        private final int index;
        private final VehicleTripDetailsResponse.RouteDetail station;

        private TargetStationMatch(int index, VehicleTripDetailsResponse.RouteDetail station) {
            this.index = index;
            this.station = station;
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
        return tripDetails.getRowCount() != null && tripDetails.getRowCount() <= 1
                && tripDetails.getRouteDetails() == null;
    }

    private String normalize(String name) {
        if (name == null) {
            return "";
        }
        return name.replaceAll("\\s+", "").toLowerCase();
    }
}