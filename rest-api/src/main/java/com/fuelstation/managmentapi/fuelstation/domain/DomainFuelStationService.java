package com.fuelstation.managmentapi.fuelstation.domain;

import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fuelstation.managmentapi.common.domain.DomainEventPublisher;
import com.fuelstation.managmentapi.common.domain.FuelGrade;
import com.fuelstation.managmentapi.fuelorder.domain.FuelOrder;
import com.fuelstation.managmentapi.fuelorder.domain.FuelOrderRepository;
import com.fuelstation.managmentapi.fuelstation.domain.events.FuelStationWasCreated;
import com.fuelstation.managmentapi.fuelstation.domain.events.FuelStationWasDeactivated;
import com.fuelstation.managmentapi.fuelstation.domain.models.FuelStation;
import com.fuelstation.managmentapi.fuelstation.domain.models.FuelStationStatus;
import com.fuelstation.managmentapi.manager.domain.Manager;
import com.fuelstation.managmentapi.manager.domain.ManagerRepository;

@Service
public class DomainFuelStationService implements FuelStationService {
    
    @Autowired
    private FuelStationFactory fuelStationFactory;

    @Autowired
    private FuelStationRepository fuelStationRepository;

    @Autowired
    private ManagerRepository managerRepository;

    @Autowired
    private FuelOrderRepository fuelOrderRepository;

    @Autowired
    private DomainEventPublisher domainEventPublisher;

    @Override
    public FuelStation createFuelStation(String street, String buildingNumber, String city, String postalCode, String country) {
        FuelStation fuelStation = fuelStationFactory.create(street, buildingNumber, city, postalCode, country);
        FuelStation savedFuelStation = fuelStationRepository.save(fuelStation);
        domainEventPublisher.publish(new FuelStationWasCreated(savedFuelStation.getId()));
        return savedFuelStation;
    }

    @Override
    public FuelStation assignManager(long fuelStationId, long managerId) {
        FuelStation fuelStation = fuelStationRepository.findById(fuelStationId).orElseThrow(() -> new NoSuchElementException("Fuel station with id:" + fuelStationId + "doesn't exist"));
        Manager manager = managerRepository.findById(managerId).orElseThrow(() -> new NoSuchElementException("Manager with id:" + managerId + "doesn't exist"));
        fuelStation.assignManager(manager);
        fuelStationRepository.save(fuelStation);
        domainEventPublisher.publishAll(fuelStation.getDomainEvents());
        return fuelStation;
    }

    @Override
    public FuelStation unassignManager(long fuelStationId, long managerId) {
        FuelStation fuelStation = fuelStationRepository.findById(fuelStationId).orElseThrow(() -> new NoSuchElementException("Fuel station with id:" + fuelStationId + "doesn't exist"));
        Manager manager = managerRepository.findById(managerId).orElseThrow(() -> new NoSuchElementException("Manager with id:" + managerId + "doesn't exist"));
        fuelStation.unassignManager(manager);
        fuelStationRepository.save(fuelStation);
        domainEventPublisher.publishAll(fuelStation.getDomainEvents());
        return fuelStation;
    }

    @Override
    public FuelStation changeFuelPrice(long fuelStationId, FuelGrade fuelGrade, float newPrice) {
        FuelStation fuelStation = fuelStationRepository.findById(fuelStationId).orElseThrow(() -> new NoSuchElementException("Fuel station with id:" + fuelStationId + "doesn't exist"));
        fuelStation.changeFuelPrice(fuelGrade, newPrice);
        fuelStationRepository.save(fuelStation);
        domainEventPublisher.publishAll(fuelStation.getDomainEvents());
        return fuelStation;
    }

    @Override
    public FuelStation processFuelDelivery(long fuelOrderId) {
        FuelOrder fuelOrder = fuelOrderRepository.findById(fuelOrderId).orElseThrow(() -> new NoSuchElementException("Fuel order with id:" + fuelOrderId + "doesn't exist")); 
        FuelStation fuelStation = fuelStationRepository.findById(fuelOrder.getFuelStationId()).orElseThrow(() -> new NoSuchElementException("Fuel station with id:" + fuelOrder.getFuelStationId() + "doesn't exist"));
        fuelStation.processFuelDelivery(fuelOrder);
        fuelStationRepository.save(fuelStation);
        domainEventPublisher.publishAll(fuelStation.getDomainEvents());
        return fuelStation;
    }

    @Override
    public FuelStation deactivateFuelStation(long fuelStationId) {
        FuelStation fuelStation = fuelStationRepository.findById(fuelStationId).orElseThrow(() -> new NoSuchElementException("Fuel station with id:" + fuelStationId + "doesn't exist"));
        fuelStation.setStatus(FuelStationStatus.Deactivated);
        fuelStation.unassignAllManagers();
        fuelStationRepository.save(fuelStation);
        domainEventPublisher.publishAll(fuelStation.getDomainEvents());
        domainEventPublisher.publish(new FuelStationWasDeactivated(fuelStationId));
        return fuelStation;
    }
}