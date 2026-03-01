package com.fuelstation.managmentapi.manager.domain;

import com.fuelstation.managmentapi.authentication.domain.User;
import com.fuelstation.managmentapi.authentication.domain.UserRole;
import com.fuelstation.managmentapi.authentication.domain.UserStatus;
import com.fuelstation.managmentapi.common.domain.Actor;
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
   private final Long managerId;
   private String firstName;
   private String lastName;
   private String fullName;
   private String email;
   private String password;
   private UserStatus status;

   public void terminate(Actor performedBy) {
      if(status == UserStatus.TERMINATED) {
         throw new ManagerAlreadyTerminatedException(managerId);
      }
      status = UserStatus.TERMINATED;
      pushDomainEvent(new ManagerTerminated(managerId, performedBy));
   }

   public User toUser() {
      return new User(managerId, firstName, lastName, fullName, email, status, UserRole.MANAGER, password);
   }

   static public Manager fromUser(User user) {
      return new Manager(
              user.getUserId(),
              user.getFirstName(),
              user.getLastName(),
              user.getFullName(),
              user.getEmail(),
              user.getPassword(),
              UserStatus.ACTIVE
      );
   }
}
