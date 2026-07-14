package dev.utsav.application.service;

import dev.utsav.application.dto.CreateEventCommand;
import dev.utsav.application.port.EventUseCase;
import dev.utsav.domain.exception.DomainException;
import dev.utsav.domain.model.Event;
import dev.utsav.domain.port.EventRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class EventService implements EventUseCase {

    private final EventRepository eventRepository;
    private final int defaultLimt;
    private final int maxLimit;

    public EventService(EventRepository eventRepository, int defaultLimit, int maxLimit) {
        this.eventRepository = eventRepository;
        this.defaultLimt = defaultLimit;
        this.maxLimit = maxLimit;
    }

    @Override
    public Event createEvent(CreateEventCommand command) {
        Event event = new Event(
                command.title(),
                command.description(),
                command.category(),
                command.venueName(),
                command.city(),
                command.startTime(),
                command.endTime(),
                command.totalSeats(),
                command.priceInPaise(),
                command.organizerId()
        );

        return eventRepository.save(event);
    }

    @Override
    public Event getEvent(UUID eventId) {
        return findOrThrow(eventId);
    }

    @Override
    public List<Event> getUpcomingEvents(String city, int limit) {
        if(limit <= 0 || limit > maxLimit){
            limit = defaultLimt;
        }
        return eventRepository.findUpcomingEventsByCity(city, limit);
    }

    @Override
    public void publishEvent(UUID eventId) {
        Event event = findOrThrow(eventId);
        event.publish();
        eventRepository.save(event);
    }



    @Override
    public void cancelEvent(UUID eventId) {
        Event event = findOrThrow(eventId);
        event.cancel();
        eventRepository.save(event);
    }


    private Event findOrThrow(UUID eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(()-> new DomainException("EVENT_NOT_FOUND", "Event with id " + eventId + " not found"));
    }
}
