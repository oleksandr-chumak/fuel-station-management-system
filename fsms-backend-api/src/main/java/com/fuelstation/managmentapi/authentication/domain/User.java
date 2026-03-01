package com.fuelstation.managmentapi.authentication.domain;

import com.fuelstation.managmentapi.common.domain.Actor;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class User {
   private final Long userId;
   private final String firstName;
   private final String lastName;
   private final String fullName;
   private final String email;
   private final UserStatus status;
   private final UserRole role;
   private final String password;

   public boolean admin() {
      return role == UserRole.ADMINISTRATOR;
   }

   public boolean manager() {
      return role == UserRole.MANAGER;
   }

   public Actor getActor() {
      return Actor.user(this.userId);
   }
}
