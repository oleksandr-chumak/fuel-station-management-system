package com.fuelstation.managmentapi.authentication.infrastructure.security;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.fuelstation.managmentapi.authentication.domain.Credentials;
import com.fuelstation.managmentapi.authentication.domain.UserRole;
import com.fuelstation.managmentapi.authentication.infrastructure.persistence.CredentialsRepository;

@Service
public class AuthenticationServiceImpl implements AuthenticationService{

    private final CredentialsRepository credentialsRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtTokenService jwtTokenService;

    public AuthenticationServiceImpl(CredentialsRepository credentialsRepository, PasswordEncoder passwordEncoder, JwtTokenService jwtTokenService) {
        this.credentialsRepository = credentialsRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenService = jwtTokenService;
    }

    @Override
    public String authenticate(String email, String password, UserRole role) {
        Credentials credentials = credentialsRepository.findByEmailAndRole(email, role)
            .orElseThrow(() -> new BadCredentialsException("Invalid credentials"));
        boolean passwordCorrect = passwordEncoder.matches(password, credentials.getPassword());
        
        if(!passwordCorrect) {
            throw new BadCredentialsException("Invalid credentials");
        }

        return jwtTokenService.generateAccessToken(new SecurityUserDetails(credentials));
    }
}
