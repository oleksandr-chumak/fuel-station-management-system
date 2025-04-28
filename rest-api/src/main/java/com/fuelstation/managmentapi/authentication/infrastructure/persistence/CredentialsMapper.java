package com.fuelstation.managmentapi.authentication.infrastructure.persistence;

import com.fuelstation.managmentapi.authentication.domain.Credentials;
import com.fuelstation.managmentapi.authentication.domain.UserRole;

public class CredentialsMapper {
    public static Credentials toDomain(CredentialsEntity entity) {
        return new Credentials(
            entity.getId(),
            getUserId(entity),
            entity.getEmail(),
            entity.getRole(),
            entity.getPassword()
        );
    }

    public static CredentialsEntity toEntity(Credentials domain) {
        return new CredentialsEntity(
            domain.getCredentialsId(),
            domain.getEmail(),
            domain.getRole(),
            domain.getPassword(),
            null,
            null
        );
    }

    private static Long getUserId(CredentialsEntity credentials) {
        if(credentials.getRole() == UserRole.Manager && credentials.getManager() != null) {
            return credentials.getManager().getId();
        } else if(credentials.getRole() == UserRole.Administrator && credentials.getAdministrator() != null){
            return credentials.getAdministrator().getId();
        } else {
            return null;
        } 
    }
}
