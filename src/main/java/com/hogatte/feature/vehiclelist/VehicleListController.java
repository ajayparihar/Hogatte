package com.hogatte.feature.vehiclelist;

import com.hogatte.bmtc.client.BmtcApiClient;
import com.hogatte.bmtc.dto.BusStopResponse;
import com.hogatte.bmtc.dto.ListVehiclesResponse;
import com.hogatte.bmtc.dto.RoutePointsResponse;
import com.hogatte.bmtc.dto.VehicleTripDetailsResponse;
import com.hogatte.feature.vehiclelist.dto.BusStopRequest;
import com.hogatte.feature.vehiclelist.dto.RoutePointsRequest;
import com.hogatte.feature.vehiclelist.dto.VehicleListRequest;
import com.hogatte.feature.vehiclelist.dto.VehicleTripDetailsRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/vehicles")
@RequiredArgsConstructor
public class VehicleListController {

    private final BmtcApiClient bmtcApiClient;

    @PostMapping("/list")
    public ResponseEntity<ListVehiclesResponse> getVehiclesPost(@Valid @RequestBody VehicleListRequest request) {
        log.info("Received POST /list request for vehicleRegNo: {}", request.getVehicleRegNo());
        ListVehiclesResponse response = bmtcApiClient.listVehicles(request.getVehicleRegNo());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/list")
    public ResponseEntity<ListVehiclesResponse> getVehiclesGet(@RequestParam(name = "vehicleRegNo", defaultValue = "KA57F5035") String vehicleRegNo) {
        log.info("Received GET /list request for vehicleRegNo: {}", vehicleRegNo);
        ListVehiclesResponse response = bmtcApiClient.listVehicles(vehicleRegNo);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/trip-details")
    public ResponseEntity<VehicleTripDetailsResponse> getVehicleTripDetailsPost(@Valid @RequestBody VehicleTripDetailsRequest request) {
        log.info("Received POST /trip-details request for vehicleId: {}", request.getVehicleId());
        VehicleTripDetailsResponse response = bmtcApiClient.getVehicleTripDetails(request.getVehicleId());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/trip-details")
    public ResponseEntity<VehicleTripDetailsResponse> getVehicleTripDetailsGet(@RequestParam(name = "vehicleId") Long vehicleId) {
        log.info("Received GET /trip-details request for vehicleId: {}", vehicleId);
        VehicleTripDetailsResponse response = bmtcApiClient.getVehicleTripDetails(vehicleId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/route-points")
    public ResponseEntity<RoutePointsResponse> getRoutePointsPost(@Valid @RequestBody RoutePointsRequest request) {
        log.info("Received POST /route-points request for routeId: {}", request.getRouteId());
        RoutePointsResponse response = bmtcApiClient.getRoutePoints(request.getRouteId());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/route-points")
    public ResponseEntity<RoutePointsResponse> getRoutePointsGet(@RequestParam(name = "routeId") Long routeId) {
        log.info("Received GET /route-points request for routeId: {}", routeId);
        RoutePointsResponse response = bmtcApiClient.getRoutePoints(routeId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/stops")
    public ResponseEntity<BusStopResponse> getBusStopsPost(@Valid @RequestBody BusStopRequest request) {
        log.info("Received POST /stops request for stationName: {}", request.getStationName());
        BusStopResponse response = bmtcApiClient.findBusStops(request.getStationName());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/stops")
    public ResponseEntity<BusStopResponse> getBusStopsGet(@RequestParam(name = "stationName") String stationName) {
        log.info("Received GET /stops request for stationName: {}", stationName);
        BusStopResponse response = bmtcApiClient.findBusStops(stationName);
        return ResponseEntity.ok(response);
    }
}
