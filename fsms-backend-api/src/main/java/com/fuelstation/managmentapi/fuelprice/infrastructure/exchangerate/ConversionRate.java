package com.fuelstation.managmentapi.fuelprice.infrastructure.exchangerate;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fuelstation.managmentapi.common.domain.CurrencyCode;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class ConversionRate {

    private String result;
    private String documentation;

    @JsonProperty("terms_of_use")
    private String termsOfUse;

    @JsonProperty("time_last_update_unix")
    private long timeLastUpdateUnix;

    @JsonProperty("time_last_update_utc")
    private String timeLastUpdateUtc;

    @JsonProperty("time_next_update_unix")
    private long timeNextUpdateUnix;

    @JsonProperty("time_next_update_utc")
    private String timeNextUpdateUtc;

    @JsonProperty("base_code")
    private CurrencyCode baseCode;

    @JsonProperty("target_code")
    private CurrencyCode targetCode;

    @JsonProperty("conversion_rate")
    private BigDecimal conversionRate;
}
