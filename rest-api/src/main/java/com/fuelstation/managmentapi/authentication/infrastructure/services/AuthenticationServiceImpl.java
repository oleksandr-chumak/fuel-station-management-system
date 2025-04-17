package com.fuelstation.managmentapi.authentication.infrastructure.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.fuelstation.managmentapi.authentication.domain.Credentials;
import com.fuelstation.managmentapi.authentication.domain.CredentialsRepository;
import com.fuelstation.managmentapi.authentication.domain.UserRole;

@Service
public class AuthenticationServiceImpl implements AuthenticationService{

    @Autowired
    private CredentialsRepository credentialsRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired 
    private JwtTokenService jwtTokenService;

    @Override
    public String authenticate(String email, String password, UserRole role) {
        Credentials credentials = credentialsRepository.findByEmailAndRole(email, role)
            .orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));
        boolean passwordIsCorrect = passwordEncoder.matches(password, credentials.getPassword());
        
        if(!passwordIsCorrect) {
            throw new IllegalArgumentException("Invalid credentials");
        }

        return jwtTokenService.generateAccessToken(credentials);
    }
}
