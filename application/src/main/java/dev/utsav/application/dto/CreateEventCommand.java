package dev.utsav.application.dto;

import dev.utsav.domain.model.enums.EventCategory;

import java.time.LocalDateTime;
import java.util.UUID;

public record CreateEventCommand(
        String title,
        String description,
        EventCategory category,
        String venueName,
        String city,
        LocalDateTime startTime,
        LocalDateTime endTime,
        int totalSeats,
        long priceInPaise,
        UUID organizerId
) {
}
