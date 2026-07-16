package dev.utsav.infrastructure.persistence;

import dev.utsav.domain.model.User;
import dev.utsav.domain.model.enums.Role;

public final class UserMapper {

    private UserMapper(){}

    public static UserJpaEntity toEntity(User user){
        UserJpaEntity entity = new UserJpaEntity();
        entity.setId(user.getId());
        entity.setEmail(user.getEmail());
        entity.setPasswordHash(user.getPasswordHash());
        entity.setDisplayName(user.getDisplayName());
        entity.setRole(user.getRole().name());
        entity.setCreatedAt(user.getCreatedAt());
        entity.setUpdatedAt(user.getUpdatedAt());
        return entity;
    }

    public static User toDomain(UserJpaEntity entity){
        return new User(
                entity.getId(),
                entity.getEmail(),
                entity.getPasswordHash(),
                entity.getDisplayName(),
                Role.valueOf(entity.getRole()),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
