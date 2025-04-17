package com.fuelstation.managmentapi.authentication.application;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Login as ADMIN user and collect access token then
 * use Authorization header as a Bearer token for
 * any route under /admin/** api request. It will work fine.
 * But if Login with USER role then use that access token
 * this api will not work
 */
@RestController
@RequestMapping("/admin")
public class AdminController {
    @GetMapping("/dashboard")
    public ResponseEntity<String> adminDashboard() {
        return ResponseEntity.ok("Welcome to the Admin Dashboard!");
    }
}