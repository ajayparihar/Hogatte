package com.hogatte.feature.vehiclelist.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class VehicleListDtoTest {

    @Test
    void testRoutePointsRequest() {
        RoutePointsRequest request1 = new RoutePointsRequest();
        request1.setRouteId(101L);

        assertEquals(101L, request1.getRouteId());

        RoutePointsRequest request2 = new RoutePointsRequest(101L);

        assertEquals(request1, request2);
        assertEquals(request1.hashCode(), request2.hashCode());
        assertTrue(request1.toString().contains("101"));
    }

    @Test
    void testVehicleListRequest() {
        VehicleListRequest request1 = new VehicleListRequest();
        request1.setVehicleRegNo("KA57F5035");

        assertEquals("KA57F5035", request1.getVehicleRegNo());

        VehicleListRequest request2 = new VehicleListRequest("KA57F5035");

        assertEquals(request1, request2);
        assertEquals(request1.hashCode(), request2.hashCode());
        assertTrue(request1.toString().contains("KA57F5035"));
    }

    @Test
    void testVehicleTripDetailsRequest() {
        VehicleTripDetailsRequest request1 = new VehicleTripDetailsRequest();
        request1.setVehicleId(15423L);

        assertEquals(15423L, request1.getVehicleId());

        VehicleTripDetailsRequest request2 = new VehicleTripDetailsRequest(15423L);

        assertEquals(request1, request2);
        assertEquals(request1.hashCode(), request2.hashCode());
        assertTrue(request1.toString().contains("15423"));
    }
}
