package com.hogatte.bmtc.client;

import com.hogatte.bmtc.dto.BusStopResponse;
import com.hogatte.bmtc.dto.ListVehiclesResponse;
import com.hogatte.bmtc.dto.RoutePointsResponse;
import com.hogatte.bmtc.dto.VehicleTripDetailsResponse;

public interface BmtcApiClient {

    ListVehiclesResponse listVehicles(String vehicleRegNo);

    VehicleTripDetailsResponse getVehicleTripDetails(Long vehicleId);

    RoutePointsResponse getRoutePoints(Long routeId);

    BusStopResponse findBusStops(String stationName);
}