package dev.utsav.infrastructure.persistence;

import dev.utsav.domain.model.Event;
import dev.utsav.domain.model.enums.EventCategory;
import dev.utsav.domain.model.enums.EventStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration test for EventPersistenceAdapter against a real (H2) database.
 *
 * @DataJpaTest boots ONLY the JPA slice of Spring: a DataSource, an
 * embedded H2 database, and our entities/repositories. No web layer, no
 * full app — just enough to talk to a database. Each test runs inside a
 * transaction that is ROLLED BACK afterward, so tests can't pollute each
 * other; every test starts from an empty database.
 *
 * @Import(EventPersistenceAdapter.class) — @DataJpaTest does NOT scan
 * general @Component beans, so we pull our adapter into the slice explicitly.
 */
@DataJpaTest
@Import(EventPersistenceAdapter.class)
class EventPersistenceAdapterTest {

    @Autowired
    private EventPersistenceAdapter adapter;

    // Test data builder — same idea as in EventTest.
    private Event newEvent() {
        return new Event(
                "Arijit Singh Live",
                "An evening of music",
                EventCategory.CONCERT,
                "JLN Stadium",
                "Delhi",
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(1).plusHours(3),
                500,
                200_000,
                UUID.randomUUID()
        );
    }

    @Test
    @DisplayName("saves an event and loads it back unchanged")
    void savesAndLoadsBack() {
        Event saved = adapter.save(newEvent());

        Optional<Event> loaded = adapter.findById(saved.getId());

        assertThat(loaded).isPresent();
        Event e = loaded.get();
        assertThat(e.getId()).isEqualTo(saved.getId());
        assertThat(e.getTitle()).isEqualTo("Arijit Singh Live");
        assertThat(e.getCategory()).isEqualTo(EventCategory.CONCERT);   // enum round-tripped
        assertThat(e.getStatus()).isEqualTo(EventStatus.DRAFT);         // state preserved
        assertThat(e.getAvailableSeats()).isEqualTo(500);
        assertThat(e.getPriceInPaise()).isEqualTo(200_000);
    }

    @Test
    @DisplayName("returns empty when the event does not exist")
    void returnsEmptyForUnknownId() {
        Optional<Event> loaded = adapter.findById(UUID.randomUUID());

        assertThat(loaded).isEmpty();
    }

    @Test
    @DisplayName("finds only published, upcoming events in the city")
    void findsUpcomingPublishedEvents() {
        Event published = newEvent();
        published.publish();          // DRAFT → PUBLISHED
        adapter.save(published);

        adapter.save(newEvent());     // a DRAFT event — must be excluded

        List<Event> upcoming = adapter.findUpcomingEventsByCity("Delhi", 10);

        assertThat(upcoming).hasSize(1);
        assertThat(upcoming.get(0).getId()).isEqualTo(published.getId());
        assertThat(upcoming.get(0).getStatus()).isEqualTo(EventStatus.PUBLISHED);
    }
}