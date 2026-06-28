package com.fuelstation.managmentapi.fuelprice.infrastructure.exchangerate;

import com.fuelstation.managmentapi.common.domain.CurrencyCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
    public class ExchangeRateClientImpl implements ExchangeRateClient {

        private final RestClient client;

        public ExchangeRateClientImpl(@Value("${exchangerate.api.key}") String apiKey) {
            this.client = RestClient.builder()
                .baseUrl("https://v6.exchangerate-api.com/v6/" + apiKey)
                .build();
        }

        @Override
        @Cacheable(value = "exchangeRates", key = "#baseCode.name() + '-' + #targetCode.name()")
        public ConversionRate getConversionRate(CurrencyCode baseCode, CurrencyCode targetCode) {
            return client.get()
                .uri("pair/%s/%s".formatted(baseCode, targetCode))
                .retrieve()
                .body(ConversionRate.class);
        }

    }
