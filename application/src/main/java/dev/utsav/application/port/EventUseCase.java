package dev.utsav.application.port;


import dev.utsav.application.dto.CreateEventCommand;
import dev.utsav.domain.model.Event;

import java.util.List;
import java.util.UUID;

//The Inbound port - what the application offers to the outside world. This is the interface that the application layer
//exposes to the outside world, and it is implemented by the application layer. The application layer is responsible
//for implementing the business logic of the application, and it uses the domain layer to do so.
//The application layer is also responsible for orchestrating the flow of data between the domain layer
//and the outside world.
public interface EventUseCase {

    Event createEvent(CreateEventCommand command);

    Event getEvent(UUID eventId);

    List<Event> getUpcomingEvents(String city, int limit);

    void publishEvent(UUID eventId);

    void cancelEvent(UUID eventId);
}

