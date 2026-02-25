package com.fuelstation.managmentapi.authentication.application;

import com.fuelstation.managmentapi.authentication.domain.Credentials;
import com.fuelstation.managmentapi.authentication.infrastructure.persistence.CredentialsRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class CredentialsFetcher {

    private final CredentialsRepository credentialsRepository;

    public Credentials fetchById(long credentialsId) {
        return credentialsRepository.findById(credentialsId)
                .orElseThrow(() -> new CredentialsNotFoundException(credentialsId));
    }

}
