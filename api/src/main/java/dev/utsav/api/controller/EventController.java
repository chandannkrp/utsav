package dev.utsav.api.controller;


import dev.utsav.api.dto.CreateEventRequest;
import dev.utsav.api.dto.EventResponse;
import dev.utsav.application.dto.CreateEventCommand;
import dev.utsav.application.port.EventUseCase;
import dev.utsav.domain.exception.DomainException;
import dev.utsav.domain.model.Event;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/events")
public class EventController {

    private final EventUseCase eventUseCase;

    public EventController(EventUseCase eventUseCase) {
        this.eventUseCase = eventUseCase;
    }

    @PostMapping
    public ResponseEntity<EventResponse> createEvent(@Valid @RequestBody CreateEventRequest request) {
        Event created = eventUseCase.createEvent(request.toCommand());
        return ResponseEntity.status(201).body(EventResponse.from(created));
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<EventResponse> getEvent(@PathVariable UUID eventId) {
        Event event = eventUseCase.getEvent(eventId);
        return ResponseEntity.ok(EventResponse.from(event));
    }

    @GetMapping
    public ResponseEntity<List<EventResponse>> getUpcoming(
            @RequestParam String city,
            @RequestParam(defaultValue = "20") int limit
    ) {
        List<Event> events = eventUseCase.getUpcomingEvents(city, limit);
        return ResponseEntity.ok(events.stream().map(EventResponse::from).toList());
    }

    @PutMapping("/{eventId}/publish")
    public ResponseEntity<Void> publishEvent(@PathVariable UUID eventId) {
        eventUseCase.publishEvent(eventId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{eventId}/cancel")
    public ResponseEntity<Void> cancelEvent(@PathVariable UUID eventId) {
        eventUseCase.cancelEvent(eventId);
        return ResponseEntity.noContent().build();
    }

}
