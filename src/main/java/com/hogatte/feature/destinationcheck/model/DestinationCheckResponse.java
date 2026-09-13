package com.hogatte.feature.destinationcheck.model;

public record DestinationCheckResponse(
                Long vehicleid,
                String vehiclenumber,
                Long stationid,
                String stationname,
                String status,
                boolean goingThere,
                String message) {
}