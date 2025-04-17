package com.fuelstation.managmentapi.authentication.domain;

public interface CredentialsFactory {
   public Credentials create(String email, String password, UserRole userRole); 
}
