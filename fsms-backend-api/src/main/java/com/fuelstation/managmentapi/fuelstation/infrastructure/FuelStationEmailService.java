package com.fuelstation.managmentapi.fuelstation.infrastructure;

public interface FuelStationEmailService {
    void sendManagerAssigned(String to, String firstName, long fuelStationId);
    void sendManagerAssigned(long managerId, long fuelStationId);
    void sendManagerUnassigned(String to, String firstName, long fuelStationId);
    void sendManagerUnassigned(long managerId, long fuelStationId);
}
