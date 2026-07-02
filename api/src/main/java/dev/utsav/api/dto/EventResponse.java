package dev.utsav.api.dto;

import dev.utsav.domain.model.Event;

import java.time.LocalDateTime;
import java.util.UUID;

public record EventResponse(
        UUID id,
        String title,
        String description,
        String category,
        String venueName,
        String city,
        LocalDateTime startTime,
        LocalDateTime endTime,
        int totalSeats,
        int availableSeats,
        long priceInPaise,
        String status
) {

    public static EventResponse from(Event event){
        return new EventResponse(
                event.getId(),
                event.getTitle(),
                event.getDescription(),
                event.getCategory().name(),
                event.getVenueName(),
                event.getCity(),
                event.getStartTime(),
                event.getEndTime(),
                event.getTotalSeats(),
                event.getAvailableSeats(),
                event.getPriceInPaise(),
                event.getStatus().name()
        );
    }
}
