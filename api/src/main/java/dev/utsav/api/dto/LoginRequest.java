package dev.utsav.api.dto;

import dev.utsav.application.dto.LoginCommand;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank String email,
        @NotBlank String password
) {

    public LoginCommand toCommand() {
        return new LoginCommand(email, password);
    }
}
