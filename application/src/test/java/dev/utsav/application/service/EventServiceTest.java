package dev.utsav.application.service;


import dev.utsav.application.dto.CreateEventCommand;
import dev.utsav.domain.exception.DomainException;
import dev.utsav.domain.model.Event;
import dev.utsav.domain.model.enums.EventCategory;
import dev.utsav.domain.model.enums.EventStatus;
import dev.utsav.domain.port.EventRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EventServiceTest {

    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    private EventService eventService;

    private CreateEventCommand validCommand(){
        return new CreateEventCommand(
                "Sample Event",
                "This is a sample event description.",
                EventCategory.CONCERT,
                "Sample Venue",
                "Sample City",
                LocalDateTime.now().plusDays(1), // start time in the future
                LocalDateTime.now().plusDays(1).plusHours(3), // end time in the future
                100,
                5000,
                UUID.randomUUID()
        );
    }

    @Test
    @DisplayName("createEvent builds an Event and saves it")
    void createEventSaves(){
        when(eventRepository.save(any(Event.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Event result = eventService.createEvent(validCommand());

        assertThat(result.getTitle()).isEqualTo("Sample Event");
        assertThat(result.getStatus()).isEqualTo(EventStatus.DRAFT);

        verify(eventRepository, times(1)).save(any(Event.class));

    }

    @Test
    @DisplayName("publishEvent loads, publishes and saves the Event")
    void publishEventFlow(){
        Event draft = new Event(
                "Sample Event",
                "This is a sample event description.",
                EventCategory.CONCERT,
                "Sample Venue",
                "Sample City",
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(1).plusHours(3),
                100,
                5000,
                UUID.randomUUID()
        );

        UUID id = draft.getId();

        //stub the event to load our draft event
        when(eventRepository.findById(id)).thenReturn(Optional.of(draft));
        when(eventRepository.save(any(Event.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        eventService.publishEvent(id);

        assertThat(draft.getStatus()).isEqualTo(EventStatus.PUBLISHED);
        verify(eventRepository).findById(id);
        verify(eventRepository).save(draft);
    }

    @Test
    @DisplayName("publishEvent throws exception if event not found")
    void publishMissingEvent(){
        UUID missingId = UUID.randomUUID();
        when(eventRepository.findById(missingId)).thenReturn(Optional.empty());

        assertThatThrownBy(()-> eventService.publishEvent(missingId))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("not found");

        verify(eventRepository, never()).save(any(Event.class));
    }

}
