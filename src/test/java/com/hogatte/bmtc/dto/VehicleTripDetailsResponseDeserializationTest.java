package com.hogatte.bmtc.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VehicleTripDetailsResponseDeserializationTest {

    @Test
    void testDeserializationWithTripStatus() throws Exception {
        String json = """
            {
                "RouteDetails": [{
                    "routeid": 5915,
                    "vehicleid": 21120,
                    "stationid": 20921,
                    "stationname": "Kempegowda Bus Station",
                    "latitude": 12.97751,
                    "longitude": 77.57141
                }],
                "LiveLocation": [{
                    "latitude": 13.017357,
                    "longitude": 77.704772,
                    "vehicleid": 21120,
                    "vehiclenumber": "KA57F5030"
                }],
                "Message": "Success",
                "RowCount": 51,
                "trip_status": "Running",
                "Issuccess": true
            }
            """;

        ObjectMapper mapper = new ObjectMapper();
        VehicleTripDetailsResponse response = mapper.readValue(json, VehicleTripDetailsResponse.class);

        assertNotNull(response);
        assertEquals("Success", response.getMessage());
        assertEquals(51, response.getRowCount());
        assertEquals("Running", response.getTripStatus());
        assertNotNull(response.getRouteDetails());
        assertEquals(1, response.getRouteDetails().size());
        assertEquals(5915L, response.getRouteDetails().get(0).getRouteid());
        assertEquals("Kempegowda Bus Station", response.getRouteDetails().get(0).getStationname());
        assertNotNull(response.getLiveLocation());
        assertEquals(1, response.getLiveLocation().size());
        assertEquals(13.017357, response.getLiveLocation().get(0).getLatitude());
    }

    @Test
    void testDeserializationWithTripstatus() throws Exception {
        String json = """
            {
                "RouteDetails": [{
                    "routeid": 5915,
                    "vehicleid": 21120,
                    "stationid": 20921,
                    "stationname": "Kempegowda Bus Station",
                    "latitude": 12.97751,
                    "longitude": 77.57141
                }],
                "LiveLocation": [{
                    "latitude": 13.017357,
                    "longitude": 77.704772,
                    "vehicleid": 21120,
                    "vehiclenumber": "KA57F5030"
                }],
                "Message": "Success",
                "RowCount": 51,
                "tripstatus": "Running",
                "Issuccess": true
            }
            """;

        ObjectMapper mapper = new ObjectMapper();
        VehicleTripDetailsResponse response = mapper.readValue(json, VehicleTripDetailsResponse.class);

        assertNotNull(response);
        assertEquals("Success", response.getMessage());
        assertEquals(51, response.getRowCount());
        assertEquals("Running", response.getTripstatus());
        assertNotNull(response.getRouteDetails());
        assertEquals(1, response.getRouteDetails().size());
    }

    @Test
    void testDeserializationWithCurrlatLong() throws Exception {
        String json = """
            {
                "RouteDetails": [{
                    "routeid": 5915,
                    "vehicleid": 21120,
                    "stationid": 20921,
                    "stationname": "Kempegowda Bus Station",
                    "latitude": 12.97751,
                    "longitude": 77.57141
                }],
                "Message": "Success",
                "RowCount": 51,
                "trip_status": "Running",
                "currlatitude": 13.017357,
                "currlongitude": 77.704772,
                "Issuccess": true
            }
            """;

        ObjectMapper mapper = new ObjectMapper();
        VehicleTripDetailsResponse response = mapper.readValue(json, VehicleTripDetailsResponse.class);

        assertNotNull(response);
        assertEquals(13.017357, response.getCurrlatitude());
        assertEquals(77.704772, response.getCurrlongitude());
    }
}