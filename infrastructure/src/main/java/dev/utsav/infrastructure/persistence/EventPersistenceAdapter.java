package dev.utsav.infrastructure.persistence;

import dev.utsav.domain.model.Event;
import dev.utsav.domain.port.EventRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class EventPersistenceAdapter implements EventRepository {

    private final SpringDataEventRepository repository;

    public EventPersistenceAdapter(SpringDataEventRepository repository) {
        this.repository = repository;
    }

    @Override
    public Event save(Event event) {
        System.out.println(">>> event id : " + event.getOrganizerId());
        EventJpaEntity entity = EventMapper.toEntity(event);
        System.out.println(">>> event id : " + event.getOrganizerId());
        EventJpaEntity saved = repository.save(entity);
        return EventMapper.toDomain(saved);
    }

    @Override
    public Optional<Event> findById(UUID eventId) {
        return repository.findById(eventId)
                .map(EventMapper::toDomain);
    }

    @Override
    public List<Event> findUpcomingEventsByCity(String city, int limit) {
        return repository.findUpcomingByCity(city, LocalDateTime.now(),
                        PageRequest.of(0, limit))
                .stream()
                .map(EventMapper::toDomain)                    // each entity → domain
                .toList();
    }

    @Override
    public boolean existsById(UUID eventId) {
        return repository.existsById(eventId);
    }

    @Override
    public void deleteById(UUID eventId) {
        repository.deleteById(eventId);
    }
}
