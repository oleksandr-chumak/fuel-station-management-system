package com.fuelstation.managmentapi.authentication.domain;

public interface UserFactory {
   User create(String email, String firstName, String lastName, String password, UserRole userRole);
}
