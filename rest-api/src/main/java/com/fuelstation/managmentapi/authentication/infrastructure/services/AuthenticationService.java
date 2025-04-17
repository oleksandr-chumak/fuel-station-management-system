package com.fuelstation.managmentapi.authentication.infrastructure.services;

import com.fuelstation.managmentapi.authentication.domain.UserRole;

public interface AuthenticationService {
   public String authenticate(String email, String password, UserRole role); 
}
