package dev.utsav.domain.port;

import dev.utsav.domain.model.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    User save(User user);

    Optional<User> findById(UUID userId);

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
}
