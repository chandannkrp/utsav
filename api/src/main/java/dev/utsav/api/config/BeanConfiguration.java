package dev.utsav.api.config;

import dev.utsav.application.port.EventUseCase;
import dev.utsav.application.service.EventService;
import dev.utsav.domain.port.EventRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

    /*
        Declares an EventUseCase bean is provided by the EventService class,
        which is constructed with an EventRepository bean.

        Spring sees this method needs an EventRepository parameter,

     */
    @Bean
    public EventUseCase eventUseCase(EventRepository eventRepository){
        return new EventService(eventRepository);
    }
}
