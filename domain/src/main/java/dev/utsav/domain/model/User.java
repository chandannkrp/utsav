package dev.utsav.domain.model;


import dev.utsav.domain.exception.DomainException;
import dev.utsav.domain.model.enums.Role;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class User {

    private final UUID id;
    private String email;
    private String passwordHash;
    private String displayName;
    private Role role;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public User(String email, String passwordHash, String displayName, Role role) {
        requireNonBlank(email, "Email");
        requireNonBlank(passwordHash, "Password hash");
        requireNonBlank(displayName, "Display name");
        Objects.requireNonNull(role, "Role must not be null");

        if(!email.contains("@")) {
            throw new DomainException("INVALID_EMAIL", "Email must be valid, got: " + email);
        }


        this.id = UUID.randomUUID();
        this.email = email;
        this.passwordHash = passwordHash;
        this.displayName = displayName;
        this.role = role;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
    }

//    Reconstitution Constructor - no need for validation again.
    public User(UUID id, String email, String passwordHash, String displayName, Role role, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.email = email;
        this.passwordHash = passwordHash;
        this.displayName = displayName;
        this.role = role;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public void changeRole(Role newRole){
        Objects.requireNonNull(newRole, "New role must not be null");
        this.role = newRole;
        this.updatedAt = LocalDateTime.now();
    }

//    Read access
    public UUID getId(){ return id; }
    public String getEmail(){ return email; }
    public String getPasswordHash(){ return passwordHash; }
    public String getDisplayName(){ return displayName; }
    public Role getRole(){ return role; }
    public LocalDateTime getCreatedAt(){ return createdAt; }
    public LocalDateTime getUpdatedAt(){ return updatedAt; }

//    Helpers
    private static void requireNonBlank(String value, String fieldName){
        if(value == null || value.isBlank()){
            throw new DomainException("BLANK_FIELD", fieldName + " must not be blank");
        }
    }

    @Override
    public boolean equals(Object o){
        if(this == o) return true;
        if(!(o instanceof User user)) return false;
        return id.equals(user.id);
    }

    @Override
    public int hashCode(){
        return id.hashCode();
    }
}
