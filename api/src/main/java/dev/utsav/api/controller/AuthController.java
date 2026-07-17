package dev.utsav.api.controller;


import dev.utsav.api.dto.AuthResponse;
import dev.utsav.api.dto.LoginRequest;
import dev.utsav.api.dto.RegisterRequest;
import dev.utsav.api.security.JwtService;
import dev.utsav.application.port.AuthUseCase;
import dev.utsav.domain.model.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Authentication", description = "Endpoints for user registration and login")
public class AuthController {

    private final AuthUseCase authUseCase;
    private final JwtService jwtService;

    public AuthController(AuthUseCase authUseCase, JwtService jwtService) {
        this.authUseCase = authUseCase;
        this.jwtService = jwtService;
    }

    @Operation(summary = "Register a new account")
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request){
        User user = authUseCase.register(request.toCommand());
        String token = jwtService.generateToken(user);
        return ResponseEntity.status(201).body(
                new AuthResponse(
                        token,
                        "Bearer",
                        user.getEmail(),
                        user.getRole().name()
                )
        );
    }

    @Operation(summary = "Login to an existing account")
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request){
        User user = authUseCase.login(request.toCommand());
        String token = jwtService.generateToken(user);
        return ResponseEntity.ok(
                new AuthResponse(
                        token,
                        "Bearer",
                        user.getEmail(),
                        user.getRole().name()
                )
        );
    }
}


