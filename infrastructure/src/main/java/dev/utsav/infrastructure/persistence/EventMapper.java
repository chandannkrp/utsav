package dev.utsav.infrastructure.persistence;

import dev.utsav.domain.model.Event;
import dev.utsav.domain.model.enums.EventCategory;
import dev.utsav.domain.model.enums.EventStatus;

public final class EventMapper {

    private EventMapper() {
        // private constructor to prevent instantiation
    }

    public static EventJpaEntity toEntity(Event event){
        EventJpaEntity entity = new EventJpaEntity();
        entity.setId(event.getId());
        entity.setTitle(event.getTitle());
        entity.setDescription(event.getDescription());
        entity.setCategory(event.getCategory().name());
        entity.setVenueName(event.getVenueName());
        entity.setCity(event.getCity());
        entity.setStartTime(event.getStartTime());
        entity.setEndTime(event.getEndTime());
        entity.setTotalSeats(event.getTotalSeats());
        entity.setAvailableSeats(event.getAvailableSeats());
        entity.setPriceInPaise(event.getPriceInPaise());
        entity.setStatus(event.getStatus().name());
        entity.setCreatedAt(event.getCreatedAt());
        entity.setUpdatedAt(event.getUpdatedAt());
        entity.setOrganizerId(event.getOrganizerId());
        return entity;
    }

    public static Event toDomain(EventJpaEntity entity){
        return new Event(
                entity.getId(),
                entity.getTitle(),
                entity.getDescription(),
                EventCategory.valueOf(entity.getCategory()),
                entity.getVenueName(),
                entity.getCity(),
                entity.getStartTime(),
                entity.getEndTime(),
                entity.getTotalSeats(),
                entity.getAvailableSeats(),
                entity.getPriceInPaise(),
                EventStatus.valueOf(entity.getStatus()),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.getOrganizerId()
                );
    }
}
