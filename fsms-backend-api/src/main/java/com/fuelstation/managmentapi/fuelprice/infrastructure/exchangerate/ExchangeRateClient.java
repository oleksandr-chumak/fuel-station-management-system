package com.fuelstation.managmentapi.fuelprice.infrastructure.exchangerate;

import com.fuelstation.managmentapi.common.domain.CurrencyCode;

public interface ExchangeRateClient {

    ConversionRate getConversionRate(CurrencyCode baseCode, CurrencyCode targetCode);
}
