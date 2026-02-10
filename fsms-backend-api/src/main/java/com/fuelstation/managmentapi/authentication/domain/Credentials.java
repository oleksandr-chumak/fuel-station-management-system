package com.fuelstation.managmentapi.authentication.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Credentials {
   private Long credentialsId;
   private String email;
   private UserRole role;
   private String username;
   private String password;

   public boolean admin() {
      return role == UserRole.ADMINISTRATOR;
   }

   public boolean manager() {
      return role == UserRole.MANAGER;
   }

}
