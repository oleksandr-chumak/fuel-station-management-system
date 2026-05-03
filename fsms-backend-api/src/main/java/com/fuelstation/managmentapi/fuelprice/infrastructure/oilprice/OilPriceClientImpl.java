package com.fuelstation.managmentapi.fuelprice.infrastructure.oilprice;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class OilPriceClientImpl implements OilPriceClient {

    private final RestClient client;

    public OilPriceClientImpl(@Value("${oilprice.api.token}") String apiToken) {
        this.client = RestClient.builder()
            .baseUrl("https://api.oilpriceapi.com")
            .defaultHeader("Authorization", "Token " + apiToken)
            .build();
    }

    @Override
    public OilPriceResponse<OilPriceBenchmark> getBenchmark(OilPriceCommodity commodity) {
        return client
            .get()
            .uri("/v1/prices/latest?by_code=" + commodity)
            .retrieve()
            .body(new ParameterizedTypeReference<>() {});
    }

}
