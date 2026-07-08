package dev.utsav.api.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import dev.utsav.api.dto.CreateEventRequest;
import dev.utsav.api.error.GlobalExceptionHandler;
import dev.utsav.application.port.EventUseCase;
import dev.utsav.domain.exception.DomainException;
import dev.utsav.domain.model.Event;
import dev.utsav.domain.model.enums.EventCategory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EventController.class)
@Import(GlobalExceptionHandler.class)
public class EventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());

    @MockitoBean
    private EventUseCase eventUseCase;

    private Event sampleEvent(){
        return new Event(
                "Sample Event",
                "This is a sample event description.",
                EventCategory.CONCERT,
                "Sample Venue",
                "Sample City",
                java.time.LocalDateTime.now().plusDays(1), // start time in the future
                java.time.LocalDateTime.now().plusDays(1).plusHours(3), // end time in the future
                100,
                5000,
                java.util.UUID.randomUUID()
        );
    }

    private CreateEventRequest validRequest(){
        return new CreateEventRequest(
                "Sample Event",
                "This is a sample event description.",
                EventCategory.CONCERT,
                "Sample Venue",
                "Sample City",
                java.time.LocalDateTime.now().plusDays(1), // start time in the future
                java.time.LocalDateTime.now().plusDays(1).plusHours(3), // end time in the future
                100,
                5000,
                java.util.UUID.randomUUID()
        );
    }

    @Test
    @DisplayName("POST /api/v1/events  - should create a new event and return 201")
    void createEvent_returns201() throws Exception{
        when(eventUseCase.createEvent(any())).thenReturn(sampleEvent());

        mockMvc.perform(
                post("/api/v1/events")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(validRequest()))
        )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Sample Event"))
                .andExpect(jsonPath("$.status").value("DRAFT"))
                .andExpect(jsonPath("$.organizerId").doesNotExist());
    }

    @Test
    @DisplayName("POST with a blank title return 400 with VALIDATION_FAILED")
    void createEvent_withBlankTitle_returns400() throws Exception{
        String badJson = """
                {
                    "title": "",
                    "description": "This is a sample event description.",
                    "category": "CONCERT",
                    "venueName": "Sample Venue",
                    "city": "Sample City",
                    "startTime": "2026-12-01T10:00:00",
                    "endTime": "2026-12-01T13:00:00",
                    "totalSeats": 100,
                    "priceInPaise": 5000,
                    "organizerId": "00000000-0000-0000-0000-000000000000"
                }
                """;

        mockMvc.perform(
                post("/api/v1/events")
                        .contentType("application/json")
                        .content(badJson)
        )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"));

    }

    @Test
    @DisplayName("GET /api/v1/events/{id} - for a missing event should return 404")
    void getEvent_notFound_returns400() throws Exception{
        UUID missingId = UUID.randomUUID();
        when(eventUseCase.getEvent(missingId))
                .thenThrow(new DomainException("EVENT_NOT_FOUND", "No event found with id " + missingId));

        mockMvc.perform(
                get("/api/v1/events/" + missingId))
                        .andExpect(status().isNotFound())
                        .andExpect(jsonPath("$.code").value("EVENT_NOT_FOUND")
        );
    }


}
