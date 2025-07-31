package com.fuelstation.managmentapi.authentication.infrastructure.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.fuelstation.managmentapi.authentication.domain.Credentials;
import com.fuelstation.managmentapi.authentication.infrastructure.persistence.CredentialsRepository;

@Service
public class UserDetailsServiceImp implements UserDetailsService {

    private final CredentialsRepository credentialsRepository;

    public UserDetailsServiceImp(CredentialsRepository credentialsRepository) {
        this.credentialsRepository = credentialsRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // TODO username shouldn't be represented as email because it can cause a conflict if we have administrator and manager with the same email address
        Credentials credentials = credentialsRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new SecurityUserDetails(credentials);


    }
}