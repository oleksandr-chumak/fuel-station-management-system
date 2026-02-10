package com.fuelstation.managmentapi.manager.domain;

import com.fuelstation.managmentapi.common.domain.AggregateRoot;
import com.fuelstation.managmentapi.manager.domain.events.ManagerTerminated;
import com.fuelstation.managmentapi.manager.domain.exceptions.ManagerAlreadyTerminatedException;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Manager extends AggregateRoot {
   private Long credentialsId;
   private String firstName;
   private String lastName;
   private ManagerStatus status;

   public void terminate() {
      if(status == ManagerStatus.TERMINATED) {
         throw new ManagerAlreadyTerminatedException(credentialsId);
      }
      status = ManagerStatus.TERMINATED;
      pushDomainEvent(new ManagerTerminated(credentialsId));
   }
}
