package com.fuelstation.managmentapi.authentication.infrastructure.persistence;

import com.fuelstation.managmentapi.authentication.domain.Credentials;

public class CredentialsMapper {
    public static Credentials toDomain(CredentialsEntity entity) {
        if (entity == null) return null;
        return new Credentials(
            entity.getId(),
            entity.getEmail(),
            entity.getRole(),
            entity.getPassword()
        );
    }

    public static CredentialsEntity toEntity(Credentials domain) {
        if (domain == null) return null;
        return new CredentialsEntity(
            domain.getId(),
            domain.getEmail(),
            domain.getRole(),
            domain.getPassword(),
            null,
            null
        );
    }
}
