package com.fuelstation.managmentapi.authentication.infrastructure.persistence;

import com.fuelstation.managmentapi.administrator.infrastructure.persistence.AdministratorEntity;
import com.fuelstation.managmentapi.authentication.domain.UserRole;
import com.fuelstation.managmentapi.manager.infrastructure.persistence.ManagerEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "credentials")
public class CredentialsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    private String password;

    @OneToOne(mappedBy = "credentials")
    private ManagerEntity manger;

    @OneToOne(mappedBy = "credentials")
    private AdministratorEntity administrator;
}