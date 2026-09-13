package com.hogatte.bmtc.client.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hogatte.bmtc.dto.BusStopResponse;
import com.hogatte.bmtc.dto.ListVehiclesResponse;
import com.hogatte.bmtc.dto.RoutePointsResponse;
import com.hogatte.bmtc.dto.VehicleTripDetailsResponse;
import com.hogatte.feature.vehiclelist.VehicleListService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;

class BmtcApiClientImplTest {

    private VehicleListService vehicleListService;
    private ObjectMapper objectMapper;
    private BmtcApiClientImpl client;

    @BeforeEach
    void setUp() {
        vehicleListService = Mockito.mock(VehicleListService.class);
        objectMapper = new ObjectMapper();
        client = new BmtcApiClientImpl(vehicleListService, objectMapper);
    }

    @Test
    void listVehicles_success() {
        String jsonResponse = """
            {
                "Message": "Success",
                "Issuccess": true,
                "data": [{
                    "vehicleid": 15423,
                    "vehicleregno": "KA57F5035",
                    "responsecode": 200
                }]
            }
            """;

        Mockito.when(vehicleListService.fetchVehicles(any())).thenReturn(jsonResponse);

        ListVehiclesResponse response = client.listVehicles("KA57F5035");

        assertNotNull(response);
        assertTrue(response.isIssuccess());
        assertEquals("Success", response.getMessage());
        assertEquals(1, response.getData().size());
        assertEquals(15423L, response.getData().get(0).getVehicleid());
        assertEquals("KA57F5035", response.getData().get(0).getVehicleregno());
        assertEquals(200, response.getData().get(0).getResponsecode());
    }

    @Test
    void getVehicleTripDetails_success() {
        String jsonResponse = """
            {
                "Message": "Success",
                "Issuccess": true,
                "RouteDetails": [{
                    "routeid": 1001,
                    "vehicleid": 15423,
                    "stationid": 50,
                    "stationname": "Majestic",
                    "sourcestation": "Majestic",
                    "destinationstation": "Silk Board",
                    "latitude": 12.9716,
                    "longitude": 77.5946,
                    "tripposition": 1
                }],
                "LiveLocation": [{
                    "latitude": 12.9716,
                    "longitude": 77.5946,
                    "vehicleid": 15423,
                    "vehiclenumber": "KA57F5035"
                }]
            }
            """;

        Mockito.when(vehicleListService.fetchVehicleTripDetails(any())).thenReturn(jsonResponse);

        VehicleTripDetailsResponse response = client.getVehicleTripDetails(15423L);

        assertNotNull(response);
        assertTrue(response.isIssuccess());
        assertEquals("Success", response.getMessage());
        assertEquals(1, response.getRouteDetails().size());
        assertEquals(1001L, response.getRouteDetails().get(0).getRouteid());
        assertEquals("Majestic", response.getRouteDetails().get(0).getStationname());
        assertEquals(1, response.getLiveLocation().size());
        assertEquals(12.9716, response.getLiveLocation().get(0).getLatitude());
    }

    @Test
    void getRoutePoints_success() {
        String jsonResponse = """
            {
                "data": [{
                    "latitude": "12.9716",
                    "longitude": "77.5946"
                }, {
                    "latitude": "12.9800",
                    "longitude": "77.6000"
                }]
            }
            """;

        Mockito.when(vehicleListService.fetchRoutePoints(any())).thenReturn(jsonResponse);

        RoutePointsResponse response = client.getRoutePoints(1001L);

        assertNotNull(response);
        assertEquals(2, response.getData().size());
        assertEquals("12.9716", response.getData().get(0).getLatitude());
        assertEquals("77.5946", response.getData().get(0).getLongitude());
    }

    @Test
    void getVehicleTripDetails_wrappedJsonString() {
        String wrappedJsonResponse = "\"{\\\"Message\\\":\\\"Success\\\",\\\"Issuccess\\\":true,\\\"RouteDetails\\\":[{\\\"routeid\\\":1001,\\\"vehicleid\\\":15423,\\\"stationid\\\":50,\\\"stationname\\\":\\\"Majestic\\\",\\\"sourcestation\\\":\\\"Majestic\\\",\\\"destinationstation\\\":\\\"Silk Board\\\",\\\"latitude\\\":12.9716,\\\"longitude\\\":77.5946}],\\\"LiveLocation\\\":[{\\\"latitude\\\":12.9716,\\\"longitude\\\":77.5946,\\\"vehicleid\\\":15423,\\\"vehiclenumber\\\":\\\"KA57F5035\\\"}]}\"";

        Mockito.when(vehicleListService.fetchVehicleTripDetails(any())).thenReturn(wrappedJsonResponse);

        VehicleTripDetailsResponse response = client.getVehicleTripDetails(15423L);

        assertNotNull(response);
        assertTrue(response.isIssuccess());
        assertEquals("Success", response.getMessage());
        assertEquals(1, response.getRouteDetails().size());
        assertEquals(1001L, response.getRouteDetails().get(0).getRouteid());
        assertEquals("Majestic", response.getRouteDetails().get(0).getStationname());
    }
}
