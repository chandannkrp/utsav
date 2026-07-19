package dev.utsav.api.controller;


import dev.utsav.api.dto.CreateEventRequest;
import dev.utsav.api.dto.EventResponse;
import dev.utsav.api.dto.EventResponseV2;
import dev.utsav.application.port.EventUseCase;
import dev.utsav.domain.model.Event;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/{version}/events")
@Tag(name = "Events", description = "Create, browse and Manage events")
public class EventController {

    private final EventUseCase eventUseCase;

    public EventController(EventUseCase eventUseCase) {
        this.eventUseCase = eventUseCase;
    }

    @PreAuthorize("hasRole('ORGANIZER') or hasRole('ADMIN')")
    @Operation(summary = "Create a new event",
    description = "Create a new event with the provided details. The event will be created in a draft state.")
    @PostMapping(version = "1")
    public ResponseEntity<EventResponse> createEvent(@Valid @RequestBody CreateEventRequest request) {
        Event created = eventUseCase.createEvent(request.toCommand());
        return ResponseEntity.status(201).body(EventResponse.from(created));
    }

    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Get an event by ID",
    description = "DEPRECATED — use v2. v1 returns price as paise")
    @GetMapping(value = "/{eventId}", version = "1")
    public ResponseEntity<EventResponse> getEvent(@PathVariable UUID eventId) {
        Event event = eventUseCase.getEvent(eventId);
        return ResponseEntity.ok()
                .header("Depreacation", "true")
                .header("Sunset", "2026-12-31T23:59:59Z")
                .header("Link", "</api/v2/events/" + eventId + ">; rel=\"successor-version\"")
                .body(EventResponse.from(event));
    }

    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Get an event by ID (v2)",
    description = "v2 : price is returned as a structured object with amount, currency and display fields")
    @GetMapping(value = "/{eventId}", version = "2")
    public ResponseEntity<EventResponseV2> getEventV2(@PathVariable UUID eventId){
        Event event = eventUseCase.getEvent(eventId);
        return ResponseEntity.ok(EventResponseV2.from(event));
    }

    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "List upcoming events in a city",
    description = "Returns a list of upcoming published events in the specified city, soonest first")
    @GetMapping(version = "1")
    public ResponseEntity<List<EventResponse>> getUpcoming(
            @RequestParam String city,
            @RequestParam(defaultValue = "20") int limit
    ) {
        List<Event> events = eventUseCase.getUpcomingEvents(city, limit);
        return ResponseEntity.ok(events.stream().map(EventResponse::from).toList());
    }

    @PreAuthorize("hasRole('ORGANIZER') or hasRole('ADMIN')")
    @Operation(summary = "Publish an event",
    description = "Transitions the event from draft to published state. Makes the event bookable by users.")
    @PutMapping(value = "/{eventId}/publish", version = "1")
    public ResponseEntity<Void> publishEvent(@PathVariable UUID eventId) {
        eventUseCase.publishEvent(eventId);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ORGANIZER') or hasRole('ADMIN')")
    @Operation(summary = "Cancel an event",
    description = "Transitions the event from published to cancelled state. Users will no longer be able to book the event.")
    @PutMapping(value = "/{eventId}/cancel", version = "1")
    public ResponseEntity<Void> cancelEvent(@PathVariable UUID eventId) {
        eventUseCase.cancelEvent(eventId);
        return ResponseEntity.noContent().build();
    }

}
