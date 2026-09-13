package com.hogatte.feature.vehiclelist;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

class VehicleListServiceTest {

    private MockRestServiceServer mockServer;
    private VehicleListService vehicleListService;

    @BeforeEach
    void setUp() {
        RestClient.Builder builder = RestClient.builder();
        mockServer = MockRestServiceServer.bindTo(builder).build();
        vehicleListService = new VehicleListService(builder.build());
    }

    @Test
    void fetchVehicles_success() {
        String jsonResponse = "{\"Message\":\"Success\",\"Issuccess\":true}";

        mockServer.expect(requestTo("/ListVehicles"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(header("User-Agent", "Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/139.0.0.0 Mobile Safari/537.36"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"vehicleRegNo\":\"KA57F5035\"}"))
                .andRespond(withSuccess(jsonResponse, MediaType.APPLICATION_JSON));

        String response = vehicleListService.fetchVehicles("KA57F5035");

        mockServer.verify();
        assertNotNull(response);
        assertEquals(jsonResponse, response);
    }

    @Test
    void fetchVehicleTripDetails_success() {
        String jsonResponse = "{\"Message\":\"Success\",\"Issuccess\":true}";

        mockServer.expect(requestTo("/VehicleTripDetails_v2"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(header("User-Agent", "Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/139.0.0.0 Mobile Safari/537.36"))
                .andExpect(header("deviceType", "WEB"))
                .andExpect(header("lan", "en"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"vehicleId\":15423}"))
                .andRespond(withSuccess(jsonResponse, MediaType.APPLICATION_JSON));

        String response = vehicleListService.fetchVehicleTripDetails(15423L);

        mockServer.verify();
        assertNotNull(response);
        assertEquals(jsonResponse, response);
    }

    @Test
    void fetchRoutePoints_success() {
        String jsonResponse = "{\"Message\":\"Success\",\"Issuccess\":true}";

        mockServer.expect(requestTo("/RoutePoints"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(header("User-Agent", "Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/139.0.0.0 Mobile Safari/537.36"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"routeid\":2357}"))
                .andRespond(withSuccess(jsonResponse, MediaType.APPLICATION_JSON));

        String response = vehicleListService.fetchRoutePoints(2357L);

        mockServer.verify();
        assertNotNull(response);
        assertEquals(jsonResponse, response);
    }
}
