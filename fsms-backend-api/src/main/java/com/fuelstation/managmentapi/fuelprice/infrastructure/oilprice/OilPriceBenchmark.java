package com.fuelstation.managmentapi.fuelprice.infrastructure.oilprice;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;

@Data
public class OilPriceBenchmark {
    private BigDecimal price;
    private String formatted;
    private String currency;
    private OilPriceCommodity code;

    @JsonProperty("created_at")
    private Instant createdAt;

    @JsonProperty("updated_at")
    private Instant updatedAt;

    private String type;
    private String unit;
    private String source;
    private Map<String, PriceChange> changes;
    private Metadata metadata;

    @Data
    public static class PriceChange {
        private BigDecimal amount;
        private BigDecimal percent;

        @JsonProperty("previous_price")
        private BigDecimal previousPrice;
    }

    @Data
    public static class Metadata {
        private String source;

        @JsonProperty("source_description")
        private String sourceDescription;
    }

}
