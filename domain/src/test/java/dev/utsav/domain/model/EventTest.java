package dev.utsav.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

import dev.utsav.domain.exception.DomainException;
import dev.utsav.domain.model.enums.EventStatus;
import org.junit.jupiter.api.DisplayName;

import dev.utsav.domain.model.enums.EventCategory;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public class EventTest {

    private static final LocalDateTime TOMORROW = LocalDateTime.now().plusDays(1);
    private static final LocalDateTime DAY_AFTER = LocalDateTime.now().plusDays(2);
    private static final UUID ORGANIZER = UUID.randomUUID();

    private Event validEvent() {
        return new Event(
            "Arijit Singh Live",
            "An evening of music", 
            EventCategory.CONCERT, 
            "JLN Stadium", 
            "Delhi", TOMORROW, 
            DAY_AFTER, 500, 
            200_000, 
            ORGANIZER);
    }


    @Nested
    @DisplayName("Creation")
    class Creation{

        @Test
        @DisplayName("a new event starts in DRAFT with all seats available")
        void newEventStartsAsDraft(){
            Event event = validEvent();

            assertThat(event.getStatus()).isEqualTo(EventStatus.DRAFT);
            assertThat(event.getAvailableSeats()).isEqualTo(500);
            assertThat(event.getId()).isNotNull();
            assertThat(event.getCreatedAt()).isNotNull();
        }

        @Test
        @DisplayName("rejects a blank title")
        void rejectBlankTitle(){
            assertThatThrownBy(() -> new Event(
                "",
                "An evening of music",
                EventCategory.CONCERT,
                "JLN Stadium",
                "Delhi", TOMORROW,
                DAY_AFTER, 500,
                200_000,
                ORGANIZER))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("Title");
        }

        @Test
        @DisplayName("rejects an end time that is not after the start time")
        void rejectEndTimeNotAfterStartTime(){
            assertThatThrownBy(() -> new Event(
                    "Test",
                    "desc",
                    EventCategory.CONCERT,
                    "Venue",
                    "City",
                    DAY_AFTER,
                    TOMORROW,
                    100,
                    1000,
                    ORGANIZER))
                    .isInstanceOf(DomainException.class)
                    .hasMessageContaining("End time must be after start time");
        }

        @Test
        @DisplayName("rejects zero or negative seats")
        void rejectZeroOrNegativeSeats(){
            assertThatThrownBy(()-> new Event(
                    "Test",
                    "desc",
                    EventCategory.CONCERT,
                    "Venue",
                    "City",
                    TOMORROW,
                    DAY_AFTER,
                    0,
                    1000,
                    ORGANIZER))
                    .isInstanceOf(DomainException.class)
                    .hasMessageContaining(" positive");
        }
    }

    @Nested
    @DisplayName("State Transitions")
    class StateTransitions{

        @Test
        @DisplayName("a draft event can be published")
        void draftEventCanBePublished(){
            Event event = validEvent();
            event.publish();
            assertThat(event.getStatus()).isEqualTo(EventStatus.PUBLISHED);
        }

        @Test
        @DisplayName("publishing an event twice is rejected")
        void publishingTwiceIsRejected(){
            Event event = validEvent();
            event.publish();

            assertThatThrownBy(event::publish)
                    .isInstanceOf(DomainException.class)
                    .hasMessageContaining("Cannot transition");
        }

        @Test
        @DisplayName("a completed event is frozen — it cannot be cancelled")
        void completedEventIsFrozen() {
            Event event = validEvent();
            event.publish();
            event.complete();

            assertThatThrownBy(event::cancel)
                    .isInstanceOf(DomainException.class)
                    .hasMessageContaining("Cannot transition");
        }
    }

    @Nested
    @DisplayName("Seat Management")
    class SeatManagement{
        @Test
        @DisplayName("reserving seats reduces availability")
        void reservingReducesAvailability() {
            Event event = validEvent();
            event.publish();

            boolean reserved = event.reserveSeats(3);

            assertThat(reserved).isTrue();
            assertThat(event.getAvailableSeats()).isEqualTo(497);
            assertThat(event.getBookedSeats()).isEqualTo(3);
        }

        @Test
        @DisplayName("reserving more seats than available returns false and changes nothing")
        void reservingTooManyReturnsFalse() {
            Event event = validEvent();
            event.publish();

            boolean reserved = event.reserveSeats(501);

            assertThat(reserved).isFalse();
            assertThat(event.getAvailableSeats()).isEqualTo(500); // untouched
        }

        @Test
        @DisplayName("selling the last seat auto-transitions to SOLD_OUT")
        void sellingLastSeatMarksSoldOut() {
            Event event = validEvent();
            event.publish();

            event.reserveSeats(500);

            assertThat(event.getStatus()).isEqualTo(EventStatus.SOLD_OUT);
            assertThat(event.isSoldOut()).isTrue();
        }

        @Test
        @DisplayName("cannot reserve seats on a draft (unpublished) event")
        void cannotReserveOnDraft() {
            Event event = validEvent();   // still DRAFT — not published

            assertThatThrownBy(() -> event.reserveSeats(1))
                    .isInstanceOf(DomainException.class)
                    .hasMessageContaining("Event is not bookable");
        }

        @Test
        @DisplayName("releasing seats on a sold-out event reopens it")
        void releasingReopensSoldOutEvent() {
            Event event = validEvent();
            event.publish();
            event.reserveSeats(500);      // now SOLD_OUT

            event.releaseSeats(5);

            assertThat(event.getStatus()).isEqualTo(EventStatus.PUBLISHED);
            assertThat(event.getAvailableSeats()).isEqualTo(5);
        }
    }
}
