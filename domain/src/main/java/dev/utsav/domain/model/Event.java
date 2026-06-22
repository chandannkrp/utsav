package dev.utsav.domain.model;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

import dev.utsav.domain.exception.DomainException;
import dev.utsav.domain.model.enums.EventCategory;
import dev.utsav.domain.model.enums.EventStatus;

public class Event {

    private final UUID id;
    private String title;
    private String description;
    private EventCategory category;
    private String venueName;
    private String city;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private int totalSeats;
    private int availableSeats;
    private long priceInPaise;
    private EventStatus status;
    private UUID organizerId;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // constructor for creating a new event
    public Event(
            String title,
            String description,
            EventCategory category,
            String venueName,
            String city,
            LocalDateTime startTime,
            LocalDateTime endTime,
            int totalSeats,
            long priceInPaise,
            UUID organizerId) {

//validating first 
        requireNonBlank(title, "Title");
        requireNonBlank(venueName, "Venue name");
        requireNonBlank(city, "City");
        Objects.requireNonNull(category, "category must not be null");
        Objects.requireNonNull(startTime, "Start time must not be null");
        Objects.requireNonNull(endTime, "End time must not be null");
        Objects.requireNonNull(organizerId, "Organizer ID must not be null");

        if (!endTime.isAfter(startTime)) {
            throw new DomainException("INVALID_TIME_RANGE",
                    "End time must be after start time");
        }
        if (totalSeats <= 0) {
            throw new DomainException("INVALID_SEATS",
                    "Total seats must be positive got : " + totalSeats);
        }
        if (priceInPaise < 0) {
            throw new DomainException("INVALID_PRICE",
                    "Price cannot be negative");
        }

// assign values
        this.id = UUID.randomUUID();
        this.title = title;
        this.description = description;
        this.category = category;
        this.venueName = venueName;
        this.city = city;
        this.startTime = startTime;
        this.endTime = endTime;
        this.totalSeats = totalSeats;
        this.availableSeats = totalSeats;
        this.priceInPaise = priceInPaise;
        this.status = EventStatus.DRAFT;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
    }

    // constructor for reconstituting from persistence
    public Event(
            UUID id,
            String title,
            String description,
            EventCategory category,
            String venueName,
            String city,
            LocalDateTime startTime,
            LocalDateTime endTime,
            int totalSeats,
            int availableSeats,
            long priceInPaise,
            EventStatus status,
            UUID organizerId,
            LocalDateTime createdAt,
            LocalDateTime updatedAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.category = category;
        this.venueName = venueName;
        this.city = city;
        this.startTime = startTime;
        this.endTime = endTime;
        this.totalSeats = totalSeats;
        this.availableSeats = availableSeats;
        this.priceInPaise = priceInPaise;
        this.status = status;
        this.organizerId = organizerId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }


    public void publish() {
        requireTransitionAllowed(EventStatus.PUBLISHED);
        if (startTime.isBefore(LocalDateTime.now())) {
            throw new DomainException("EVENT_IN_PAST",
                    "Cannot publish an event whose start time has already passed"
            );
        }
        this.status = EventStatus.PUBLISHED;
        touch();
    }

    public boolean reserveSeats(int count) {
        if (status != EventStatus.PUBLISHED) {
            throw new DomainException("NOT_BOOKABLE",
                    "Event is not bookable, current status" + status
            );
        }
        if (count <= 0) {
            throw new DomainException("INVALID_SEAT_COUNT",
                    "Seat count must be positive"
            );
        }
        if (availableSeats < count) {
            return false;
        }
        this.availableSeats -= count;
        if (this.availableSeats == 0) {
            this.status = EventStatus.SOLD_OUT;
        }
        touch();
        return true;
    }

    public void releaseSeats(int count) {
        if (count <= 0 || count > getBookedSeats()) {
            throw new DomainException("INVALID_RELEASE",
                    "Cannot release " + count + " seats");
        }
        this.availableSeats += count;
        // if we were sold out and seats just freed up, reopen for booking
        if (this.status == EventStatus.SOLD_OUT && this.availableSeats > 0) {
            this.status = EventStatus.PUBLISHED;
        }
        touch();
    }

    public void cancel() {
        requireTransitionAllowed(EventStatus.CANCELLED);
        this.status = EventStatus.CANCELLED;
        touch();
    }

    public void complete() {
        requireTransitionAllowed(EventStatus.COMPLETED);
        this.status = EventStatus.COMPLETED;
        touch();
    }

// Read access
    public UUID getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public EventCategory getCategory() {
        return category;
    }

    public String getVenueName() {
        return venueName;
    }

    public String getCity() {
        return city;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public int getTotalSeats() {
        return totalSeats;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    public long getPriceInPaise() {
        return priceInPaise;
    }

    public EventStatus getStatus() {
        return status;
    }

    public UUID getOrganizerId() {
        return organizerId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public int getBookedSeats() {
        return totalSeats - availableSeats;
    }

    public boolean isSoldOut() {
        return availableSeats == 0;
    }

    private void touch() {
        this.updatedAt = LocalDateTime.now();
    }

    private void requireTransitionAllowed(EventStatus target) {
        if (!status.canTransitionTo(target)) {
            throw new DomainException("INVALID_TRANSITION",
                    "Cannot transition from " + status + " to " + target);
        }
    }

    private static void requireNonBlank(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new DomainException("BLANK_FIELD", fieldName + " must not be blank");
        }
    }

}
