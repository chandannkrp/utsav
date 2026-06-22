package dev.utsav.domain.port;

import dev.utsav.domain.model.Event;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EventRepository {

    Event save(Event event);

    Optional<Event> findById(UUID eventId);

    List<Event> findUpcomingEventsByCity(String city, int limit);

    boolean existsById(UUID eventId);

    void deleteById(UUID eventId);

}
