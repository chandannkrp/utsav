package dev.utsav.api.config;


import jakarta.servlet.*;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;

@Component
public class LoggingFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("Incoming request : "+ servletRequest.getRemoteAddr() + " " + servletRequest.getLocalAddr() + " " + servletRequest.getLocalName());
        filterChain.doFilter(servletRequest, servletResponse);
        System.out.println("Outgoing response : "+ servletResponse.getContentType() + " " + servletResponse.getCharacterEncoding());
    }

}


