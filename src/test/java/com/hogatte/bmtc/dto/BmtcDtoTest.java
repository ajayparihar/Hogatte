package com.hogatte.bmtc.dto;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BmtcDtoTest {

    @Test
    void testListVehiclesResponseAndVehicle() {
        ListVehiclesResponse.Vehicle vehicle = new ListVehiclesResponse.Vehicle();
        vehicle.setVehicleid(100L);
        vehicle.setVehicleregno("KA01F1234");
        vehicle.setResponsecode(200);

        assertEquals(100L, vehicle.getVehicleid());
        assertEquals("KA01F1234", vehicle.getVehicleregno());
        assertEquals(200, vehicle.getResponsecode());

        ListVehiclesResponse.Vehicle vehicle2 = new ListVehiclesResponse.Vehicle();
        vehicle2.setVehicleid(100L);
        vehicle2.setVehicleregno("KA01F1234");
        vehicle2.setResponsecode(200);

        assertEquals(vehicle, vehicle2);
        assertEquals(vehicle.hashCode(), vehicle2.hashCode());
        assertTrue(vehicle.toString().contains("KA01F1234"));

        ListVehiclesResponse response = new ListVehiclesResponse();
        response.setMessage("Success");
        response.setIssuccess(true);
        response.setData(List.of(vehicle));

        assertEquals("Success", response.getMessage());
        assertTrue(response.isIssuccess());
        assertEquals(1, response.getData().size());
        assertEquals(vehicle, response.getData().get(0));

        ListVehiclesResponse response2 = new ListVehiclesResponse();
        response2.setMessage("Success");
        response2.setIssuccess(true);
        response2.setData(List.of(vehicle2));

        assertEquals(response, response2);
        assertEquals(response.hashCode(), response2.hashCode());
        assertTrue(response.toString().contains("Success"));
    }

    @Test
    void testRoutePointsResponseAndEntry() {
        RoutePointsResponse.RoutePointEntry entry = new RoutePointsResponse.RoutePointEntry();
        entry.setLatitude("12.90");
        entry.setLongitude("77.50");

        assertEquals("12.90", entry.getLatitude());
        assertEquals("77.50", entry.getLongitude());

        RoutePointsResponse.RoutePointEntry entry2 = new RoutePointsResponse.RoutePointEntry();
        entry2.setLatitude("12.90");
        entry2.setLongitude("77.50");

        assertEquals(entry, entry2);
        assertEquals(entry.hashCode(), entry2.hashCode());
        assertTrue(entry.toString().contains("12.90"));

        RoutePointsResponse response = new RoutePointsResponse();
        response.setData(List.of(entry));

        assertNotNull(response.getData());
        assertEquals(1, response.getData().size());
        assertEquals(entry, response.getData().get(0));

        RoutePointsResponse response2 = new RoutePointsResponse();
        response2.setData(List.of(entry2));

        assertEquals(response, response2);
        assertEquals(response.hashCode(), response2.hashCode());
    }

    @Test
    void testVehicleTripDetailsResponseAndNestedClasses() {
        VehicleTripDetailsResponse.RouteDetail detail = new VehicleTripDetailsResponse.RouteDetail();
        detail.setRouteid(101L);
        detail.setVehicleid(202L);
        detail.setStationid(303L);
        detail.setStationname("Station A");
        detail.setSourcestation("Station Start");
        detail.setDestinationstation("Station End");
        detail.setLatitude(12.34);
        detail.setLongitude(56.78);
        detail.setTripposition(1);

        assertEquals(101L, detail.getRouteid());
        assertEquals(202L, detail.getVehicleid());
        assertEquals(303L, detail.getStationid());
        assertEquals("Station A", detail.getStationname());
        assertEquals("Station Start", detail.getSourcestation());
        assertEquals("Station End", detail.getDestinationstation());
        assertEquals(12.34, detail.getLatitude());
        assertEquals(56.78, detail.getLongitude());
        assertEquals(1, detail.getTripposition());

        VehicleTripDetailsResponse.LiveLocationEntry liveLoc = new VehicleTripDetailsResponse.LiveLocationEntry();
        liveLoc.setLatitude(12.34);
        liveLoc.setLongitude(56.78);
        liveLoc.setVehicleid(202L);
        liveLoc.setVehiclenumber("KA01F1234");

        assertEquals(12.34, liveLoc.getLatitude());
        assertEquals(56.78, liveLoc.getLongitude());
        assertEquals(202L, liveLoc.getVehicleid());
        assertEquals("KA01F1234", liveLoc.getVehiclenumber());

        VehicleTripDetailsResponse response = new VehicleTripDetailsResponse();
        response.setRouteDetails(List.of(detail));
        response.setLiveLocation(List.of(liveLoc));
        response.setMessage("OK");
        response.setIssuccess(true);

        assertEquals(1, response.getRouteDetails().size());
        assertEquals(1, response.getLiveLocation().size());
        assertEquals("OK", response.getMessage());
        assertTrue(response.isIssuccess());

        VehicleTripDetailsResponse response2 = new VehicleTripDetailsResponse();
        response2.setRouteDetails(List.of(detail));
        response2.setLiveLocation(List.of(liveLoc));
        response2.setMessage("OK");
        response2.setIssuccess(true);

        assertEquals(response, response2);
        assertEquals(response.hashCode(), response2.hashCode());
        assertTrue(response.toString().contains("OK"));
    }
}
