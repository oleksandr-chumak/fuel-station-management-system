package com.fuelstation.managmentapi.authentication.infrastructure.security;

import com.fuelstation.managmentapi.authentication.domain.UserRole;

public interface AuthenticationService {
   String authenticate(String email, String password, UserRole role);
}
