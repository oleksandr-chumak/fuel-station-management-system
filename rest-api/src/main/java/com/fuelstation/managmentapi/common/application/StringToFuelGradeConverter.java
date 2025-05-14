package com.fuelstation.managmentapi.common.application;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.fuelstation.managmentapi.common.domain.FuelGrade;

@Component
public class StringToFuelGradeConverter implements Converter<String, FuelGrade> {
    @Override
    public FuelGrade convert(String source) {
        for (FuelGrade grade : FuelGrade.values()) {
            if (grade.toString().equalsIgnoreCase(source) ||
                    grade.name().equalsIgnoreCase(source)) {
                return grade;
            }
        }
        throw new IllegalArgumentException("Unknown fuel grade: " + source);
    }
}