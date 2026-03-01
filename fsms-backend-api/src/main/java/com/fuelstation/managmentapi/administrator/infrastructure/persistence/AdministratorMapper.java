package com.fuelstation.managmentapi.administrator.infrastructure.persistence;

import com.fuelstation.managmentapi.authentication.domain.UserRole;
import com.fuelstation.managmentapi.authentication.domain.UserStatus;
import com.fuelstation.managmentapi.authentication.infrastructure.persistence.UserEntity;
import org.springframework.stereotype.Component;

import com.fuelstation.managmentapi.administrator.domain.Administrator;

@Component
public class AdministratorMapper {

    public Administrator toDomain(UserEntity entity) {
        return new Administrator(
                entity.getUserId(),
                entity.getFirstName(),
                entity.getLastName(),
                entity.getFullName(),
                entity.getEmail(),
                entity.getPassword(),
                UserStatus.fromId(entity.getUserStatusId())
        );
    }

    public UserEntity toEntity(Administrator domain) {
        return new UserEntity(
                domain.getAdministratorId(),
                domain.getFirstName(),
                domain.getLastName(),
                domain.getFullName(),
                domain.getEmail(),
                domain.getStatus().getId(),
                UserRole.ADMINISTRATOR.getId(),
                domain.getPassword()
        );
    }
}