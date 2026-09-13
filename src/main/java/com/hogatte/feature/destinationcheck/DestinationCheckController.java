package com.hogatte.feature.destinationcheck;

import com.hogatte.feature.destinationcheck.model.DestinationCheckRequest;
import com.hogatte.feature.destinationcheck.model.DestinationCheckResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping({"/api/v1", ""})
@RequiredArgsConstructor
public class DestinationCheckController {

    private final DestinationCheckService destinationCheckService;

    @PostMapping("/destination-check")
    public DestinationCheckResponse checkPost(@Valid @RequestBody DestinationCheckRequest request) {
        log.info("Received POST /destination-check request for vehicleId: {}, stationName: {}", request.getVehicleid(), request.getStationName());
        return destinationCheckService.check(request);
    }

    @GetMapping("/destination-check")
    public DestinationCheckResponse checkGet(
            @RequestParam(name = "vehicleid", required = false) Long vehicleid,
            @RequestParam(name = "vehicleId", required = false) Long vehicleId,
            @RequestParam(name = "stationName", required = false) String stationName,
            @RequestParam(name = "stationname", required = false) String stationname,
            @RequestParam(name = "StopName", required = false) String stopNameParam,
            @RequestParam(name = "stopName", required = false) String stopNameParam2
    ) {
        Long finalVehicleId = vehicleid != null ? vehicleid : vehicleId;
        String finalStationName = stationName != null ? stationName :
                (stationname != null ? stationname :
                        (stopNameParam != null ? stopNameParam : stopNameParam2));
        DestinationCheckRequest request = new DestinationCheckRequest(finalVehicleId, finalStationName);
        log.info("Received GET /destination-check request for vehicleId: {}, stationName: {}", finalVehicleId, finalStationName);
        return destinationCheckService.check(request);
    }
}