package com.hogatte.feature.destinationcheck.impl;

import com.hogatte.bmtc.client.BmtcApiClient;
import com.hogatte.bmtc.dto.VehicleTripDetailsResponse;
import com.hogatte.feature.destinationcheck.model.DestinationCheckRequest;
import com.hogatte.feature.destinationcheck.model.DestinationCheckResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DestinationCheckServiceImplTest {

    @Mock
    private BmtcApiClient bmtcApiClient;

    @InjectMocks
    private DestinationCheckServiceImpl destinationCheckService;

    private VehicleTripDetailsResponse.RouteDetail createStop(Long stationId, String name, double lat, double lon) {
        VehicleTripDetailsResponse.RouteDetail stop = new VehicleTripDetailsResponse.RouteDetail();
        stop.setRouteid(5915L);
        stop.setStationid(stationId);
        stop.setStationname(name);
        stop.setLatitude(lat);
        stop.setLongitude(lon);
        return stop;
    }

    @Test
    void check_targetStationAhead_returnsAhead() {
        // Station 1: KBS (index 0)
        VehicleTripDetailsResponse.RouteDetail stop1 = createStop(20921L, "Kempegowda Bus Station", 12.97751, 77.57141);
        // Station 2: Tin Factory (index 1)
        VehicleTripDetailsResponse.RouteDetail stop2 = createStop(21229L, "Tin Factory", 12.99698, 77.66937);
        // Station 3: NES Office (index 2)
        VehicleTripDetailsResponse.RouteDetail stop3 = createStop(23219L, "NES Office", 13.03605, 77.7358);

        // Vehicle is near Station 1 (Kempegowda Bus Station)
        VehicleTripDetailsResponse.LiveLocationEntry liveLocation = new VehicleTripDetailsResponse.LiveLocationEntry();
        liveLocation.setVehicleid(21120L);
        liveLocation.setVehiclenumber("KA57F5030");
        liveLocation.setLatitude(12.97755);
        liveLocation.setLongitude(77.57145);

        VehicleTripDetailsResponse tripDetails = new VehicleTripDetailsResponse();
        tripDetails.setMessage("Success");
        tripDetails.setRouteDetails(List.of(stop1, stop2, stop3));
        tripDetails.setLiveLocation(List.of(liveLocation));

        when(bmtcApiClient.getVehicleTripDetails(21120L)).thenReturn(tripDetails);

        // Checking target station NES Office
        DestinationCheckRequest request = new DestinationCheckRequest(21120L, "NES Office");
        DestinationCheckResponse response = destinationCheckService.check(request);

        assertNotNull(response);
        assertEquals(21120L, response.vehicleid());
        assertEquals("KA57F5030", response.vehiclenumber());
        assertEquals(23219L, response.stationid());
        assertEquals("NES Office", response.stationname());
        assertEquals("AHEAD", response.status());
        assertTrue(response.goingThere());
        assertEquals("Bus is heading towards your destination", response.message());
    }

    @Test
    void check_targetStationPassed_returnsPassed() {
        // Station 1: KBS (index 0)
        VehicleTripDetailsResponse.RouteDetail stop1 = createStop(20921L, "Kempegowda Bus Station", 12.97751, 77.57141);
        // Station 2: NES Office (index 1)
        VehicleTripDetailsResponse.RouteDetail stop2 = createStop(23219L, "NES Office", 13.03605, 77.7358);
        // Station 3: Budigere (index 2)
        VehicleTripDetailsResponse.RouteDetail stop3 = createStop(30214L, "Budigere", 13.13512, 77.74753);

        // Vehicle is near Station 3 (Budigere)
        VehicleTripDetailsResponse.LiveLocationEntry liveLocation = new VehicleTripDetailsResponse.LiveLocationEntry();
        liveLocation.setVehicleid(21120L);
        liveLocation.setVehiclenumber("KA57F5030");
        liveLocation.setLatitude(13.134974);
        liveLocation.setLongitude(77.747416);

        VehicleTripDetailsResponse tripDetails = new VehicleTripDetailsResponse();
        tripDetails.setMessage("Success");
        tripDetails.setRouteDetails(List.of(stop1, stop2, stop3));
        tripDetails.setLiveLocation(List.of(liveLocation));

        when(bmtcApiClient.getVehicleTripDetails(21120L)).thenReturn(tripDetails);

        // Checking target station NES Office - vehicle has already passed it
        DestinationCheckRequest request = new DestinationCheckRequest(21120L, "NES Office");
        DestinationCheckResponse response = destinationCheckService.check(request);

        assertNotNull(response);
        assertEquals(21120L, response.vehicleid());
        assertEquals("KA57F5030", response.vehiclenumber());
        assertEquals(23219L, response.stationid());
        assertEquals("NES Office", response.stationname());
        assertEquals("PASSED", response.status());
        assertFalse(response.goingThere());
        assertEquals("Bus has already passed your destination", response.message());
    }

    @Test
    void check_stationNotFoundOnRoute_returnsNotFound() {
        VehicleTripDetailsResponse.RouteDetail stop1 = createStop(20921L, "Kempegowda Bus Station", 12.97751, 77.57141);
        VehicleTripDetailsResponse.RouteDetail stop2 = createStop(21229L, "Tin Factory", 12.99698, 77.66937);

        VehicleTripDetailsResponse.LiveLocationEntry liveLocation = new VehicleTripDetailsResponse.LiveLocationEntry();
        liveLocation.setVehicleid(21120L);
        liveLocation.setVehiclenumber("KA57F5030");
        liveLocation.setLatitude(12.97755);
        liveLocation.setLongitude(77.57145);

        VehicleTripDetailsResponse tripDetails = new VehicleTripDetailsResponse();
        tripDetails.setMessage("Success");
        tripDetails.setRouteDetails(List.of(stop1, stop2));
        tripDetails.setLiveLocation(List.of(liveLocation));

        when(bmtcApiClient.getVehicleTripDetails(21120L)).thenReturn(tripDetails);

        // Requesting unknown station
        DestinationCheckRequest request = new DestinationCheckRequest(21120L, "Unknown Stop");
        DestinationCheckResponse response = destinationCheckService.check(request);

        assertNotNull(response);
        assertEquals(21120L, response.vehicleid());
        assertEquals("KA57F5030", response.vehiclenumber());
        assertNull(response.stationid());
        assertEquals("Unknown Stop", response.stationname());
        assertEquals("NOT_FOUND", response.status());
        assertFalse(response.goingThere());
        assertEquals("Station is not on this vehicle's current route", response.message());
    }

    @Test
    void check_noTripInProgress_returnsNoTrip() {
        VehicleTripDetailsResponse tripDetails = new VehicleTripDetailsResponse();
        tripDetails.setMessage("No Records Found");
        tripDetails.setRowCount(1);

        when(bmtcApiClient.getVehicleTripDetails(21120L)).thenReturn(tripDetails);

        DestinationCheckRequest request = new DestinationCheckRequest(21120L, "NES Office");
        DestinationCheckResponse response = destinationCheckService.check(request);

        assertNotNull(response);
        assertEquals("NO_TRIP", response.status());
        assertFalse(response.goingThere());
        assertEquals("No trip is currently in progress for this vehicle", response.message());
    }

    @Test
    void check_usesCurrlatLongFallback() {
        VehicleTripDetailsResponse.RouteDetail stop1 = createStop(20921L, "Kempegowda Bus Station", 12.97751, 77.57141);
        VehicleTripDetailsResponse.RouteDetail stop2 = createStop(23219L, "NES Office", 13.03605, 77.7358);

        VehicleTripDetailsResponse tripDetails = new VehicleTripDetailsResponse();
        tripDetails.setMessage("Success");
        tripDetails.setRouteDetails(List.of(stop1, stop2));
        tripDetails.setLiveLocation(null);
        tripDetails.setCurrlatitude(12.97755);
        tripDetails.setCurrlongitude(77.57145);

        when(bmtcApiClient.getVehicleTripDetails(21120L)).thenReturn(tripDetails);

        DestinationCheckRequest request = new DestinationCheckRequest(21120L, "NES Office");
        DestinationCheckResponse response = destinationCheckService.check(request);

        assertEquals("AHEAD", response.status());
        assertTrue(response.goingThere());
    }

    @Test
    void check_matchesIgnoringSpacesAndCase_rajagopalanagara() {
        // Station with spaced name on route
        VehicleTripDetailsResponse.RouteDetail stop1 = createStop(20921L, "Kempegowda Bus Station", 12.97751, 77.57141);
        VehicleTripDetailsResponse.RouteDetail stop2 = createStop(23219L, "Rajagopala Nagara", 13.03605, 77.7358);

        VehicleTripDetailsResponse.LiveLocationEntry liveLocation = new VehicleTripDetailsResponse.LiveLocationEntry();
        liveLocation.setVehicleid(21120L);
        liveLocation.setLatitude(12.97755);
        liveLocation.setLongitude(77.57145);

        VehicleTripDetailsResponse tripDetails = new VehicleTripDetailsResponse();
        tripDetails.setMessage("Success");
        tripDetails.setRouteDetails(List.of(stop1, stop2));
        tripDetails.setLiveLocation(List.of(liveLocation));

        when(bmtcApiClient.getVehicleTripDetails(21120L)).thenReturn(tripDetails);

        // Requested without space and different casing: "rajagopalanagara"
        DestinationCheckRequest request = new DestinationCheckRequest(21120L, "rajagopalanagara");
        DestinationCheckResponse response = destinationCheckService.check(request);

        assertEquals("AHEAD", response.status());
        assertEquals("Rajagopala Nagara", response.stationname());
        assertEquals(23219L, response.stationid());
        assertTrue(response.goingThere());

        // Inverted: if user sends "Rajagopala  Nagara" with extra spaces
        DestinationCheckRequest request2 = new DestinationCheckRequest(21120L, "  Rajagopala   Nagara  ");
        DestinationCheckResponse response2 = destinationCheckService.check(request2);

        assertEquals("AHEAD", response2.status());
        assertEquals("Rajagopala Nagara", response2.stationname());
        assertTrue(response2.goingThere());
    }
}
