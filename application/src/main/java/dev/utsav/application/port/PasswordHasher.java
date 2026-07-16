package dev.utsav.application.port;

public interface PasswordHasher {

    String hashPassword(String password);

    boolean matches(String rawPassword, String hashedPassword);
}
