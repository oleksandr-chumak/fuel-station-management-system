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

import com.fuelstation.managmentapi.administrator.domain.Administrator;
import com.fuelstation.managmentapi.administrator.infrastructure.persistence.AdministratorRepository;
import com.fuelstation.managmentapi.authentication.domain.Credentials;
import com.fuelstation.managmentapi.authentication.domain.UserRole;
import com.fuelstation.managmentapi.authentication.infrastructure.persistence.CredentialsRepository;
import com.fuelstation.managmentapi.authentication.infrastructure.security.AuthenticationService;
import com.fuelstation.managmentapi.manager.domain.Manager;
import com.fuelstation.managmentapi.manager.infrastructure.persistence.ManagerRepository;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationService authService;

    @Autowired
    private CredentialsRepository credentialsRepository;

    @Autowired
    private AdministratorRepository administratorRepository;

    @Autowired
    private ManagerRepository managerRepository;

    @PostMapping("/login/admin")
    public ResponseEntity<String> loginAdmin(@RequestBody AuthRequest authRequest) {
        String accessToken = authService.authenticate(authRequest.getEmail(), authRequest.getPassword(),
                UserRole.ADMINISTRATOR);
        return new ResponseEntity<String>(accessToken, HttpStatus.OK);
    }

    @PostMapping("/login/manager")
    public ResponseEntity<String> loginManager(@RequestBody AuthRequest authRequest) {
        String accessToken = authService.authenticate(authRequest.getEmail(), authRequest.getPassword(),
                UserRole.MANAGER);
        return new ResponseEntity<String>(accessToken, HttpStatus.OK);
    }

    @GetMapping("/me")
    public ResponseEntity<CredentialsResponse> getCurrentUser(Authentication authentication) {
        // Todo make it as a use case
        Credentials credentials = credentialsRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new NoSuchElementException("User doesn't exist"));

        long userId = -1;

        switch (credentials.getRole()) {
            case UserRole.MANAGER:
                // TODO handle a case when manager is not found 
                Manager manager = managerRepository.findByCredentialsId(credentials.getCredentialsId())
                    .orElseThrow(() -> new IllegalStateException("Manager not found by credentials id"));
                userId = manager.getId();
                break;
            case UserRole.ADMINISTRATOR:
                // TODO handle a case when administrator is not found 
                Administrator administrator = administratorRepository.findByCredentialsId(credentials.getCredentialsId())
                    .orElseThrow(() -> new IllegalStateException("Administrator not found by credentials id"));
                userId = administrator.getId();
                break;
            default:
                // TODO handle a case when role is unsupported 
                throw new IllegalStateException("Unsupported user role: " + credentials.getRole());
        }

        return ResponseEntity.ok(CredentialsResponse.fromDomain(credentials, userId));
    }
}