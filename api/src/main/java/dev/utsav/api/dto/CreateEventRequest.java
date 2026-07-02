package dev.utsav.api.dto;

import dev.utsav.application.dto.CreateEventCommand;
import dev.utsav.domain.model.enums.EventCategory;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;
import java.util.UUID;

public record CreateEventRequest(

        @NotBlank(message = "Title must not be blank")
        @Size(max = 200, message = "Title must not exceed 200 characters")
        String title,

        @Size(max = 2000, message = "Description must be at most 2000 characters")
        String description,

        @NotNull(message = "Category is required")
        EventCategory category,

        @NotBlank(message = "Venue name must not be blank")
        String venueName,

        @NotBlank(message = "City must not be blank")
        String city,

        @NotNull(message = "Start time is required")
        @Future(message = "Start time must be in the future")
        LocalDateTime startTime,

        @NotNull(message = "End time is required")
        @Future(message = "End time must be in the future")
        LocalDateTime endTime,

        @Positive(message = "Total seats must be a positive number")
        int totalSeats,

        @PositiveOrZero(message = "Price in paise must be zero or a positive number")
        long priceInPaise,

        @NotNull(message = "Organizer ID is required")
        UUID organizerId
) {

    public CreateEventCommand toCommand(){
        return new CreateEventCommand(
                title, description, category, venueName, city,
                startTime, endTime, totalSeats, priceInPaise, organizerId
        );
    }
}
