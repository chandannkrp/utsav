package dev.utsav.api.config;

import dev.utsav.application.port.AuthUseCase;
import dev.utsav.application.port.EventUseCase;
import dev.utsav.application.port.PasswordHasher;
import dev.utsav.application.service.AuthService;
import dev.utsav.application.service.EventService;
import dev.utsav.domain.port.EventRepository;
import dev.utsav.domain.port.UserRepository;
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
    public EventUseCase eventUseCase(EventRepository eventRepository, EventProperties eventProperties) {
        return new EventService(eventRepository, eventProperties.defaultLimit(), eventProperties.maxLimit());
    }


    @Bean
    public AuthUseCase authUseCase(UserRepository userRepository, PasswordHasher passwordHasher){
        return new AuthService(userRepository, passwordHasher);
    }
}
