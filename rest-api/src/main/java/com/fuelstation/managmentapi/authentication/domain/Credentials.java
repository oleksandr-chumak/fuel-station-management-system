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
   private String password;
}
