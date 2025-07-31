package com.fuelstation.managmentapi.authentication.application;

import com.fuelstation.managmentapi.authentication.application.usecases.GetMe;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fuelstation.managmentapi.authentication.domain.UserRole;
import com.fuelstation.managmentapi.authentication.infrastructure.security.AuthenticationService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationService authService;

    private final GetMe getMe;

    public AuthController(AuthenticationService authService, GetMe getMe) {
        this.authService = authService;
        this.getMe = getMe;
    }

    @PostMapping("/login/admin")
    public ResponseEntity<String> loginAdmin(@RequestBody @Valid AuthRequest authRequest) {
        String accessToken = authService.authenticate(authRequest.getEmail(), authRequest.getPassword(),
                UserRole.ADMINISTRATOR);
        return new ResponseEntity<>(accessToken, HttpStatus.OK);
    }

    @PostMapping("/login/manager")
    public ResponseEntity<String> loginManager(@RequestBody @Valid AuthRequest authRequest) {
        String accessToken = authService.authenticate(authRequest.getEmail(), authRequest.getPassword(), UserRole.MANAGER);
        return new ResponseEntity<>(accessToken, HttpStatus.OK);
    }

    @GetMapping("/me")
    public ResponseEntity<Me> getCurrentUser(Authentication authentication) {
        return ResponseEntity.ok(getMe.process(authentication.getName()));
    }

}