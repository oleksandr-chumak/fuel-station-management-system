package com.fuelstation.managmentapi.authentication.application;

import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fuelstation.managmentapi.authentication.domain.Credentials;
import com.fuelstation.managmentapi.authentication.domain.CredentialsRepository;
import com.fuelstation.managmentapi.authentication.domain.UserRole;
import com.fuelstation.managmentapi.authentication.infrastructure.services.AuthenticationService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationService authService;

    @Autowired
    private CredentialsRepository credentialsRepository;

    @PostMapping("/login/admin") 
    public ResponseEntity<String> loginAdmin(@RequestBody AuthRequest authRequest) {
        String accessToken = authService.authenticate(authRequest.getEmail(), authRequest.getPassword(), UserRole.Administrator);
        return new ResponseEntity<String>(accessToken, HttpStatus.OK);
    }
  
    @PostMapping("/login/manager") 
    public ResponseEntity<String> loginManager(@RequestBody AuthRequest authRequest) {
        String accessToken = authService.authenticate(authRequest.getEmail(), authRequest.getPassword(), UserRole.Manager);
        return new ResponseEntity<String>(accessToken, HttpStatus.OK);
    }

    @GetMapping("/me")
    public ResponseEntity<CredentialsResponse> getCurrentUser(Authentication authentication) {
        Credentials credentials = credentialsRepository.findByEmail(authentication.getName())
            .orElseThrow(() -> new NoSuchElementException("User doesn't exist"));
        return new ResponseEntity<>(CredentialsResponse.fromDomain(credentials), HttpStatus.OK);
    }
}