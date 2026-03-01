package com.fuelstation.managmentapi.administrator.domain;

import com.fuelstation.managmentapi.authentication.domain.User;
import com.fuelstation.managmentapi.authentication.domain.UserRole;
import com.fuelstation.managmentapi.authentication.domain.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Administrator {
    private final Long administratorId;
    private final String firstName;
    private final String lastName;
    private final String fullName;
    private final String email;
    private final String password;
    private final UserStatus status;

    public User toUser() {
        return new User(administratorId, firstName, lastName, fullName, email, status, UserRole.ADMINISTRATOR, password);
    }

    public static Administrator fromUser(User user) {
        return new Administrator(
                user.getUserId(),
                user.getFirstName(),
                user.getLastName(),
                user.getFullName(),
                user.getEmail(),
                user.getPassword(),
                user.getStatus()
        );
    }
}