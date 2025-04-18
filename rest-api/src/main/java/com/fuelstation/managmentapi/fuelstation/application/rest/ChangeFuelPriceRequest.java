package com.fuelstation.managmentapi.fuelstation.application.rest;

public class ChangeFuelPriceRequest {
    private String fuelGrade;
    private float newPrice;

    // Getters and Setters
    public String getFuelGrade() {
        return fuelGrade;
    }

    public void setFuelGrade(String fuelGrade) {
        this.fuelGrade = fuelGrade;
    }

    public float getNewPrice() {
        return newPrice;
    }

    public void setNewPrice(float newPrice) {
        this.newPrice = newPrice;
    }
}