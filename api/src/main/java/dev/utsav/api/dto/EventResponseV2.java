package dev.utsav.api.dto;

import dev.utsav.domain.model.Event;

import java.time.LocalDateTime;
import java.util.UUID;

public record EventResponseV2(
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
        MoneyView price,
        String status
) {

    public static EventResponseV2 from(Event event){
        return new EventResponseV2(
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
                MoneyView.ofPaise(event.getPriceInPaise()),
                event.getStatus().name()
        );
    }
}
