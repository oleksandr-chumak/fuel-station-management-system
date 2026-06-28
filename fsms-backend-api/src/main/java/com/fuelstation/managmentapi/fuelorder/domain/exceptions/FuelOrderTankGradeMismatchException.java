package com.fuelstation.managmentapi.fuelorder.domain.exceptions;

import com.fuelstation.managmentapi.fuelgrade.domain.FuelGrade;
import org.springframework.http.HttpStatus;

public class FuelOrderTankGradeMismatchException extends FuelOrderDomainException {

    public FuelOrderTankGradeMismatchException(
        long fuelTankId,
        FuelGrade tankGrade,
        FuelGrade orderGrade,
        long fuelStationId
    ) {
        super(
            String.format(
                "Fuel tank %d at station %d holds grade %s, which does not match the ordered grade %s.",
                fuelTankId,
                fuelStationId,
                tankGrade,
                orderGrade
            ),
            HttpStatus.BAD_REQUEST,
            "TANK_GRADE_MISMATCH"
        );
    }

}