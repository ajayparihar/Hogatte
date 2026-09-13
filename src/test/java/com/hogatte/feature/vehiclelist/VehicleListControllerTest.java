package com.hogatte.feature.vehiclelist;

import com.hogatte.bmtc.client.BmtcApiClient;
import com.hogatte.bmtc.dto.ListVehiclesResponse;
import com.hogatte.bmtc.dto.RoutePointsResponse;
import com.hogatte.bmtc.dto.VehicleTripDetailsResponse;
import com.hogatte.exception.GlobalExceptionHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(VehicleListController.class)
@Import(GlobalExceptionHandler.class)
class VehicleListControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @MockitoBean
        private BmtcApiClient bmtcApiClient;

        @Test
        void testGetVehiclesPostSuccess() throws Exception {
                ListVehiclesResponse mockResponse = new ListVehiclesResponse();
                mockResponse.setMessage("Success");
                mockResponse.setIssuccess(true);
                ListVehiclesResponse.Vehicle vehicle = new ListVehiclesResponse.Vehicle();
                vehicle.setVehicleid(15423L);
                vehicle.setVehicleregno("KA57F5035");
                vehicle.setResponsecode(200);
                mockResponse.setData(java.util.List.of(vehicle));

                when(bmtcApiClient.listVehicles(any()))
                                .thenReturn(mockResponse);

                mockMvc.perform(post("/api/v1/vehicles/list")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"vehicleRegNo\":\"KA57F5035\"}"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.Issuccess").value(true))
                                .andExpect(jsonPath("$.data[0].vehicleid").value(15423))
                                .andExpect(jsonPath("$.data[0].vehicleregno").value("KA57F5035"));
        }

        @Test
        void testGetVehiclesGetSuccess() throws Exception {
                ListVehiclesResponse mockResponse = new ListVehiclesResponse();
                mockResponse.setMessage("Success");
                mockResponse.setIssuccess(true);
                ListVehiclesResponse.Vehicle vehicle = new ListVehiclesResponse.Vehicle();
                vehicle.setVehicleid(15423L);
                vehicle.setVehicleregno("KA57F5035");
                vehicle.setResponsecode(200);
                mockResponse.setData(java.util.List.of(vehicle));

                when(bmtcApiClient.listVehicles(any()))
                                .thenReturn(mockResponse);

                mockMvc.perform(get("/api/v1/vehicles/list")
                                .param("vehicleRegNo", "KA57F5035"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.Issuccess").value(true))
                                .andExpect(jsonPath("$.data[0].vehicleid").value(15423));
        }

        @Test
        void testGetVehiclesGetDefaultParamSuccess() throws Exception {
                ListVehiclesResponse mockResponse = new ListVehiclesResponse();
                mockResponse.setMessage("Success");
                mockResponse.setIssuccess(true);
                ListVehiclesResponse.Vehicle vehicle = new ListVehiclesResponse.Vehicle();
                vehicle.setVehicleid(15423L);
                vehicle.setVehicleregno("KA57F5035");
                vehicle.setResponsecode(200);
                mockResponse.setData(java.util.List.of(vehicle));

                when(bmtcApiClient.listVehicles(any()))
                                .thenReturn(mockResponse);

                mockMvc.perform(get("/api/v1/vehicles/list"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.Issuccess").value(true));
        }

        @Test
        void testGetVehiclesPostValidationFailure() throws Exception {
                mockMvc.perform(post("/api/v1/vehicles/list")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"vehicleRegNo\":\"\"}"))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.error").exists());
        }

        @Test
        void testGetVehicleTripDetailsPostSuccess() throws Exception {
                VehicleTripDetailsResponse mockResponse = new VehicleTripDetailsResponse();
                mockResponse.setMessage("Success");
                mockResponse.setIssuccess(true);
                VehicleTripDetailsResponse.RouteDetail routeDetail = new VehicleTripDetailsResponse.RouteDetail();
                routeDetail.setVehicleid(15423L);
                routeDetail.setRouteid(1001L);
                mockResponse.setRouteDetails(java.util.List.of(routeDetail));

                when(bmtcApiClient.getVehicleTripDetails(any()))
                                .thenReturn(mockResponse);

                mockMvc.perform(post("/api/v1/vehicles/trip-details")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"vehicleId\":15423}"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.Issuccess").value(true))
                                .andExpect(jsonPath("$.RouteDetails[0].vehicleid").value(15423));
        }

        @Test
        void testGetVehicleTripDetailsGetSuccess() throws Exception {
                VehicleTripDetailsResponse mockResponse = new VehicleTripDetailsResponse();
                mockResponse.setMessage("Success");
                mockResponse.setIssuccess(true);
                VehicleTripDetailsResponse.RouteDetail routeDetail = new VehicleTripDetailsResponse.RouteDetail();
                routeDetail.setVehicleid(15423L);
                routeDetail.setRouteid(1001L);
                mockResponse.setRouteDetails(java.util.List.of(routeDetail));

                when(bmtcApiClient.getVehicleTripDetails(any()))
                                .thenReturn(mockResponse);

                mockMvc.perform(get("/api/v1/vehicles/trip-details")
                                .param("vehicleId", "15423"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.Issuccess").value(true))
                                .andExpect(jsonPath("$.RouteDetails[0].vehicleid").value(15423));
        }

        @Test
        void testGetVehicleTripDetailsPostValidationFailure() throws Exception {
                mockMvc.perform(post("/api/v1/vehicles/trip-details")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{}"))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.error").exists());
        }

        @Test
        void testGetRoutePointsPostSuccess() throws Exception {
                RoutePointsResponse mockResponse = new RoutePointsResponse();
                RoutePointsResponse.RoutePointEntry point = new RoutePointsResponse.RoutePointEntry();
                point.setLatitude("12.9716");
                point.setLongitude("77.5946");
                mockResponse.setData(java.util.List.of(point));

                when(bmtcApiClient.getRoutePoints(any()))
                                .thenReturn(mockResponse);

                mockMvc.perform(post("/api/v1/vehicles/route-points")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"routeId\":2357}"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.data[0].latitude").value("12.9716"));
        }

        @Test
        void testGetRoutePointsGetSuccess() throws Exception {
                RoutePointsResponse mockResponse = new RoutePointsResponse();
                RoutePointsResponse.RoutePointEntry point = new RoutePointsResponse.RoutePointEntry();
                point.setLatitude("12.9716");
                point.setLongitude("77.5946");
                mockResponse.setData(java.util.List.of(point));

                when(bmtcApiClient.getRoutePoints(any()))
                                .thenReturn(mockResponse);

                mockMvc.perform(get("/api/v1/vehicles/route-points")
                                .param("routeId", "2357"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.data[0].latitude").value("12.9716"));
        }

        @Test
        void testGetRoutePointsPostValidationFailure() throws Exception {
                mockMvc.perform(post("/api/v1/vehicles/route-points")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{}"))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.error").exists());
        }
}
