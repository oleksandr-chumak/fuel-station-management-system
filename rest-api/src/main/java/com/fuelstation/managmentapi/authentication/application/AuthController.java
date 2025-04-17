package com.fuelstation.managmentapi.authentication.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fuelstation.managmentapi.authentication.domain.UserRole;
import com.fuelstation.managmentapi.authentication.infrastructure.services.AuthenticationService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationService authService;

    @PostMapping("/login/admin") 
    public ResponseEntity<String> loginAdmin(@RequestBody AuthRequest authRequest) {
        String accessToken = authService.authenticate(authRequest.getUsername(), authRequest.getPassword(), UserRole.Administrator);
        return new ResponseEntity<String>(accessToken, HttpStatus.OK);
    }
  
    @PostMapping("/login/manager") 
    public ResponseEntity<String> loginManager(@RequestBody AuthRequest authRequest) {
        String accessToken = authService.authenticate(authRequest.getUsername(), authRequest.getPassword(), UserRole.Manager);
        return new ResponseEntity<String>(accessToken, HttpStatus.OK);
    }
}