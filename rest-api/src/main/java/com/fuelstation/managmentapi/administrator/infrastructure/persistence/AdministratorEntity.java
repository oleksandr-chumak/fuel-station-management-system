package com.fuelstation.managmentapi.administrator.infrastructure.persistence;

import com.fuelstation.managmentapi.authentication.infrastructure.persistence.CredentialsEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "administrators")
public class AdministratorEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // TODO idk is it right
    @OneToOne(
        cascade = { 
            CascadeType.MERGE,      // Allow merging detached entities
            CascadeType.REFRESH,    // Refresh from the database
            CascadeType.DETACH      // Detach from persistence context
        }, 
        fetch = FetchType.EAGER
    )
    @JoinColumn(name = "credentials_id", referencedColumnName = "id")
    private CredentialsEntity credentials;
}
