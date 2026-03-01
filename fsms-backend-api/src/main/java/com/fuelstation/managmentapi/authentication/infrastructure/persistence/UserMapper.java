package com.fuelstation.managmentapi.authentication.infrastructure.persistence;

import com.fuelstation.managmentapi.authentication.domain.User;
import com.fuelstation.managmentapi.authentication.domain.UserRole;
import com.fuelstation.managmentapi.authentication.domain.UserStatus;

public class UserMapper {
    public static User toDomain(UserEntity entity) {
        return new User(
            entity.getUserId(),
            entity.getFirstName(),
            entity.getLastName(),
            entity.getFullName(),
            entity.getEmail(),
            UserStatus.fromId(entity.getUserStatusId()),
            UserRole.fromId(entity.getUserRoleId()),
            entity.getPassword()
        );
    }

    public static UserEntity toEntity(User domain) {
        return new UserEntity(
            domain.getUserId(),
            domain.getFirstName(),
            domain.getLastName(),
            domain.getFullName(),
            domain.getEmail(),
            domain.getStatus().getId(),
            domain.getRole().getId(),
            domain.getPassword()
        );
    }
}
