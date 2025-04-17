package com.fuelstation.managmentapi.common.domain;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AggregateRoot {
   private transient List<DomainEvent> domainEvents = new ArrayList<>();

   protected void pushDomainEvent(DomainEvent domainEvent) {
        domainEvents.add(domainEvent);
   }
}
