package com.fuelstation.managmentapi.authentication.application;

import com.fuelstation.managmentapi.authentication.application.usecases.GetManagerAccessToken;
import com.fuelstation.managmentapi.authentication.application.usecases.GetMe;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.fuelstation.managmentapi.authentication.domain.UserRole;
import com.fuelstation.managmentapi.authentication.infrastructure.security.AuthenticationService;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {

    private final AuthenticationService authService;
    private final GetManagerAccessToken getManagerAccessToken;
    private final GetMe getMe;

    @PostMapping("/admins/login")
    public ResponseEntity<String> loginAdmin(@RequestBody @Valid AuthRequest authRequest) {
        var accessToken = authService.authenticate(authRequest.getEmail(), authRequest.getPassword(),
                UserRole.ADMINISTRATOR);
        return new ResponseEntity<>(accessToken, HttpStatus.OK);
    }

    @PostMapping("/managers/login")
    public ResponseEntity<String> loginManager(@RequestBody @Valid AuthRequest authRequest) {
        var accessToken = authService.authenticate(authRequest.getEmail(), authRequest.getPassword(), UserRole.MANAGER);
        return new ResponseEntity<>(accessToken, HttpStatus.OK);
    }

    @GetMapping("/managers/{managerId}/token")
    public ResponseEntity<String> getManagerAccessToken(@PathVariable Long managerId) {
        var accessToken = getManagerAccessToken.process(managerId);
        return new ResponseEntity<>(accessToken, HttpStatus.OK);
    }

    @GetMapping("/me")
    public ResponseEntity<Me> getCurrentUser(Authentication authentication) {
        return ResponseEntity.ok(getMe.process(authentication.getName()));
    }

}