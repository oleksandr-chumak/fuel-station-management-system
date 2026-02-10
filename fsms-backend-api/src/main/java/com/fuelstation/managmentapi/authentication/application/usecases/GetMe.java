package com.fuelstation.managmentapi.authentication.application.usecases;


import com.fuelstation.managmentapi.authentication.application.Me;
import com.fuelstation.managmentapi.authentication.domain.Credentials;
import com.fuelstation.managmentapi.authentication.infrastructure.persistence.CredentialsRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class GetMe {

    private final CredentialsRepository credentialsRepository;

    @Transactional
    public Me process(String username) {
        Credentials credentials = credentialsRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found"));

        return new Me(credentials.getCredentialsId(), credentials.getEmail(), credentials.getRole().toString());
    }
}
