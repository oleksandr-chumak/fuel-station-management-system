package com.fuelstation.managmentapi.manager.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Manager {
   private Long id; 
   private String firstName;
   private String lastName;
   private ManagerStatus status;
   // email need to be taken from credentials
   private String email; 
   private Long credentialsId; 
}
