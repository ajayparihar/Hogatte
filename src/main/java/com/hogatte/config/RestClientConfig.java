package com.hogatte.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Bean
    public RestClient bmtcRestClient() {
        return RestClient.builder()
                .baseUrl("https://bmtcmobileapi.karnataka.gov.in/WebAPI")
                .defaultHeader("Origin", "https://nammabmtcapp.karnataka.gov.in")
                .defaultHeader("Referer", "https://nammabmtcapp.karnataka.gov.in/")
                .defaultHeader("Accept", MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
}