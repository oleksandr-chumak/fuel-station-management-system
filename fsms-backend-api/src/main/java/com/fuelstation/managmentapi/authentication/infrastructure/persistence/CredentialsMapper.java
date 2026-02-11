package com.fuelstation.managmentapi.authentication.infrastructure.persistence;

import com.fuelstation.managmentapi.authentication.domain.Credentials;
import com.fuelstation.managmentapi.authentication.domain.UserRole;

public class CredentialsMapper {
    public static Credentials toDomain(CredentialsEntity entity) {
        return new Credentials(
            entity.getCredentialsId(),
            entity.getEmail(),
            UserRole.fromId(entity.getUserRoleId()),
            entity.getUsername(),
            entity.getPassword()
        );
    }

    public static CredentialsEntity toEntity(Credentials domain) {
        return new CredentialsEntity(
            domain.getCredentialsId(),
            domain.getEmail(),
            domain.getRole().getId(),
            domain.getUsername(),
            domain.getPassword()
        );
    }
}
