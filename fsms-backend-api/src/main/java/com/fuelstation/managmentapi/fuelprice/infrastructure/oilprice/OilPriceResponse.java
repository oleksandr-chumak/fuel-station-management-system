package com.fuelstation.managmentapi.fuelprice.infrastructure.oilprice;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OilPriceResponse<T> {
    private final String status;
    private final T data;
}
