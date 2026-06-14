package com.fuelstation.managmentapi.manager.application.query;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import com.fuelstation.managmentapi.manager.domain.Manager;
import com.fuelstation.managmentapi.manager.infrastructure.persistence.ManagerRepository;

@Component
@RequiredArgsConstructor
public class ListManagersQuery {

    private final ManagerRepository managerRepository;

    public List<Manager> process() {
        return managerRepository.findAll();
    }

}
