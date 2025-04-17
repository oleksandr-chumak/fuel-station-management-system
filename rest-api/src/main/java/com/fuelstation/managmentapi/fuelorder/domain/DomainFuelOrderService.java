package com.fuelstation.managmentapi.fuelorder.domain;

import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;

import com.fuelstation.managmentapi.common.domain.FuelGrade;

public class DomainFuelOrderService implements FuelOrderService {

    @Autowired
    private FuelOrderFactory fuelOrderFactory;

    @Autowired FuelOrderRepository fuelOrderRepository;

    @Override
    public FuelOrder createFuelOrder(Long gasStationId, FuelGrade fuelGrade, float amount) {
        FuelOrder fuelOrder = fuelOrderFactory.create(gasStationId, fuelGrade, amount); 
        return fuelOrderRepository.save(fuelOrder);
    }

    @Override
    public FuelOrder confirmFuelOrder(Long fuelOrderId) {
        FuelOrder fuelOrder = fuelOrderRepository.findById(fuelOrderId).orElseThrow(() -> new NoSuchElementException("Fuel order with id:" + fuelOrderId + "doesn't exist"));
        fuelOrder.confirm();
        return fuelOrderRepository.save(fuelOrder);
    }

    @Override
    public FuelOrder rejectFuelOrder(Long fuelOrderId) {
        FuelOrder fuelOrder = fuelOrderRepository.findById(fuelOrderId).orElseThrow(() -> new NoSuchElementException("Fuel order with id:" + fuelOrderId + "doesn't exist"));
        fuelOrder.reject();
        return fuelOrderRepository.save(fuelOrder);
    }
}
