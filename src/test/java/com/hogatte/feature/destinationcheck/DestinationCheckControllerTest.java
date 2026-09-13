package com.hogatte.feature.destinationcheck;

import com.hogatte.exception.GlobalExceptionHandler;
import com.hogatte.feature.destinationcheck.model.DestinationCheckRequest;
import com.hogatte.feature.destinationcheck.model.DestinationCheckResponse;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DestinationCheckController.class)
@Import(GlobalExceptionHandler.class)
class DestinationCheckControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DestinationCheckService destinationCheckService;

    @Test
    void testCheckSuccess_ahead() throws Exception {
        Mockito.when(destinationCheckService.check(any(DestinationCheckRequest.class)))
                .thenReturn(new DestinationCheckResponse(21120L, "KA57F5030", 23219L, "Avalahalli Hosakote", "AHEAD", true, "Bus is heading towards your destination"));

        mockMvc.perform(post("/api/v1/destination-check")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"vehicleid\":21120,\"stationName\":\"Avalahalli Hosakote\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.vehicleid").value(21120))
                .andExpect(jsonPath("$.vehiclenumber").value("KA57F5030"))
                .andExpect(jsonPath("$.stationid").value(23219))
                .andExpect(jsonPath("$.stationname").value("Avalahalli Hosakote"))
                .andExpect(jsonPath("$.status").value("AHEAD"))
                .andExpect(jsonPath("$.goingThere").value(true));
    }

    @Test
    void testCheckSuccess_passed() throws Exception {
        Mockito.when(destinationCheckService.check(any(DestinationCheckRequest.class)))
                .thenReturn(new DestinationCheckResponse(21120L, "KA57F5030", 23219L, "Avalahalli Hosakote", "PASSED", false, "Bus has already passed your destination"));

        mockMvc.perform(post("/destination-check")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"vehicleid\":21120,\"stationName\":\"Avalahalli Hosakote\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("PASSED"))
                .andExpect(jsonPath("$.goingThere").value(false));
    }

    @Test
    void testCheckGet_success() throws Exception {
        Mockito.when(destinationCheckService.check(any(DestinationCheckRequest.class)))
                .thenReturn(new DestinationCheckResponse(21120L, "KA57F5030", 23219L, "Avalahalli Hosakote", "PASSED", false, "Bus has already passed your destination"));

        mockMvc.perform(get("/api/v1/destination-check")
                        .param("vehicleid", "21120")
                        .param("stationName", "Avalahalli Hosakote"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("PASSED"));
    }

    @Test
    void testCheckValidationFailure_nullVehicleid() throws Exception {
        mockMvc.perform(post("/api/v1/destination-check")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"vehicleid\":null,\"stationName\":\"NES Office\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());
    }

    @Test
    void testCheckValidationFailure_blankStationName() throws Exception {
        mockMvc.perform(post("/api/v1/destination-check")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"vehicleid\":21120,\"stationName\":\"\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());
    }
}
