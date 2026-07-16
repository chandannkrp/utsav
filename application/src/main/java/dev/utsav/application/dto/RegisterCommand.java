package dev.utsav.application.dto;

import dev.utsav.domain.model.enums.Role;

public record RegisterCommand(
        String email,
        String rawPassword,
        String displayName,
        Role role
) {
}
