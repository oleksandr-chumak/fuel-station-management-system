package com.fuelstation.managmentapi.manager.domain;

import com.fuelstation.managmentapi.common.domain.AggregateRoot;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Manager extends AggregateRoot {
   private Long id; 
   private String firstName;
   private String lastName;
   private ManagerStatus status;
   private Long credentialsId; 

   public void terminate() {
      if(status == ManagerStatus.Deactivated) {
         throw new IllegalArgumentException("Manager is already terminated");
      }
      status = ManagerStatus.Deactivated;
      pushDomainEvent(new ManagerWasTerminated(id));
   }
}
