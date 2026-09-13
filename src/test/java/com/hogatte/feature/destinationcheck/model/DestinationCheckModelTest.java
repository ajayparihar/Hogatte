package com.hogatte.feature.destinationcheck.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DestinationCheckModelTest {

    @Test
    void testDestinationCheckRequest() {
        DestinationCheckRequest request = new DestinationCheckRequest();
        request.setVehicleid(21120L);
        request.setStationName("NES Office");

        assertEquals(21120L, request.getVehicleid());
        assertEquals("NES Office", request.getStationName());

        DestinationCheckRequest request2 = new DestinationCheckRequest(21120L, "NES Office");
        assertEquals(21120L, request2.getVehicleid());
        assertEquals("NES Office", request2.getStationName());

        assertEquals(request.getVehicleid(), request2.getVehicleid());
    }

    @Test
    void testDestinationCheckResponse() {
        DestinationCheckResponse responseAhead = new DestinationCheckResponse(
                21120L,
                "KA57F5030",
                23219L,
                "NES Office",
                "AHEAD",
                true,
                "Bus is heading towards your destination"
        );

        DestinationCheckResponse responsePassed = new DestinationCheckResponse(
                21120L,
                "KA57F5030",
                23219L,
                "NES Office",
                "PASSED",
                false,
                "Bus has already passed your destination"
        );

        assertEquals(21120L, responseAhead.vehicleid());
        assertEquals("KA57F5030", responseAhead.vehiclenumber());
        assertEquals(23219L, responseAhead.stationid());
        assertEquals("NES Office", responseAhead.stationname());
        assertEquals("AHEAD", responseAhead.status());
        assertTrue(responseAhead.goingThere());
        assertEquals("Bus is heading towards your destination", responseAhead.message());

        assertFalse(responsePassed.goingThere());
        assertEquals("PASSED", responsePassed.status());
        assertEquals("Bus has already passed your destination", responsePassed.message());

        DestinationCheckResponse copy = new DestinationCheckResponse(
                21120L,
                "KA57F5030",
                23219L,
                "NES Office",
                "AHEAD",
                true,
                "Bus is heading towards your destination"
        );
        assertEquals(responseAhead, copy);
        assertEquals(responseAhead.hashCode(), copy.hashCode());
    }
}
