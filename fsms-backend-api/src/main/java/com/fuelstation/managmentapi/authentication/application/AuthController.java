package com.fuelstation.managmentapi.authentication.application;

import com.fuelstation.managmentapi.authentication.application.query.GetManagerAccessTokenQuery;
import com.fuelstation.managmentapi.authentication.application.query.GetMeQuery;
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
    private final GetManagerAccessTokenQuery getManagerAccessTokenQuery;
    private final GetMeQuery getMeQuery;

    @PostMapping("/admins/login")
    public ResponseEntity<String> loginAdmin(@RequestBody @Valid AuthRequest authRequest) {
        var accessToken = authService.authenticate(
                authRequest.getEmail(),
                authRequest.getPassword(),
                UserRole.ADMINISTRATOR
        );
        return new ResponseEntity<>(accessToken, HttpStatus.OK);
    }

    @PostMapping("/managers/login")
    public ResponseEntity<String> loginManager(@RequestBody @Valid AuthRequest authRequest) {
        var accessToken = authService.authenticate(authRequest.getEmail(), authRequest.getPassword(), UserRole.MANAGER);
        return new ResponseEntity<>(accessToken, HttpStatus.OK);
    }

    @GetMapping("/managers/{managerId}/token")
    public ResponseEntity<String> getManagerAccessToken(@PathVariable Long managerId) {
        var accessToken = getManagerAccessTokenQuery.process(managerId);
        return new ResponseEntity<>(accessToken, HttpStatus.OK);
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser(Authentication authentication) {
        return ResponseEntity.ok(UserResponse.fromUser(getMeQuery.process(authentication.getName())));
    }

}
