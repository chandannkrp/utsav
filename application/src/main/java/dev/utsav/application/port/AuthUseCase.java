package dev.utsav.application.port;

import dev.utsav.application.dto.LoginCommand;
import dev.utsav.application.dto.RegisterCommand;
import dev.utsav.domain.model.User;

public interface AuthUseCase {

    User register(RegisterCommand command);

    User login(LoginCommand command);
}
