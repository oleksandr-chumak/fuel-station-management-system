package com.fuelstation.managmentapi.manager.infrastructure.persistence;

import com.fuelstation.managmentapi.authentication.domain.UserRole;
import com.fuelstation.managmentapi.authentication.infrastructure.persistence.UserEntity;
import com.fuelstation.managmentapi.authentication.domain.UserStatus;
import org.springframework.stereotype.Component;

import com.fuelstation.managmentapi.manager.domain.Manager;

@Component
public class ManagerMapper {

    public Manager toDomain(UserEntity entity) {
        return new Manager(
                entity.getUserId(),
                entity.getFirstName(),
                entity.getLastName(),
                entity.getFullName(),
                entity.getEmail(),
                entity.getPassword(),
                UserStatus.fromId(entity.getUserStatusId())
        );
    }

    public UserEntity toEntity(Manager domain) {
        return new UserEntity(
                domain.getManagerId(),
                domain.getFirstName(),
                domain.getLastName(),
                domain.getFullName(),
                domain.getEmail(),
                domain.getStatus().getId(),
                UserRole.MANAGER.getId(),
                domain.getPassword()
        );
    }
}