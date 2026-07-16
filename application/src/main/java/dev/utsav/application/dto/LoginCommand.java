package dev.utsav.application.dto;

public record LoginCommand(
        String email,
        String rawPassword
) {
}
