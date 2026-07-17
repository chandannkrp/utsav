package dev.utsav.api.dto;

import dev.utsav.application.dto.RegisterCommand;
import dev.utsav.domain.model.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RegisterRequest(

        @NotBlank @Email(message = "Must be a valid email address")
        String email,

        @NotBlank @Size(min = 8, message = "Password must be at least 8 characters long")
        String password,

        @NotBlank
        String displayName,

        @NotNull(message = "Role is required")
        Role role

) {

    public RegisterCommand toCommand(){
        return new RegisterCommand(
                email,
                password,
                displayName,
                role
        );
    }
}
