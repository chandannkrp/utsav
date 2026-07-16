package dev.utsav.application.service;

import dev.utsav.application.dto.LoginCommand;
import dev.utsav.application.dto.RegisterCommand;
import dev.utsav.application.port.AuthUseCase;
import dev.utsav.application.port.PasswordHasher;
import dev.utsav.domain.exception.DomainException;
import dev.utsav.domain.model.User;
import dev.utsav.domain.port.UserRepository;

public class AuthService implements AuthUseCase {

    private final UserRepository userRepository;
    private final PasswordHasher passwordHasher;

    public AuthService(UserRepository userRepository, PasswordHasher passwordHasher) {
        this.userRepository = userRepository;
        this.passwordHasher = passwordHasher;
    }

    @Override
    public User register(RegisterCommand command) {
        if(userRepository.existsByEmail(command.email().toLowerCase())) {
            throw new DomainException("EMAIL_ALREADY_EXISTS", "Email already exists: " + command.email());
        }

        String hashedPassword = passwordHasher.hashPassword(command.rawPassword());

        User user = new User(
                command.email(),
                hashedPassword,
                command.displayName(),
                command.role()
        );

        return userRepository.save(user);
    }

    @Override
    public User login(LoginCommand command) {
        User user = userRepository.findByEmail(command.email().toLowerCase())
                .orElseThrow(()-> new DomainException("INVALID_CREDENTIALS", "Invalid Email or password" + command.email()));

        if(!passwordHasher.matches(command.rawPassword(), user.getPasswordHash())) {
            throw new DomainException("INVALID_CREDENTIALS", "Invalid Email or password" + command.email());
        }

        return user;
    }
}
