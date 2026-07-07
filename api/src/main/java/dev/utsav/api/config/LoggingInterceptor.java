package dev.utsav.api.config;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class LoggingInterceptor implements HandlerInterceptor {

    public boolean preHandle(){
        System.out.println("Incoming request intercepted : ");
        return true;
    }
}