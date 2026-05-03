package com.fuelstation.managmentapi.fuelprice.infrastructure.oilprice;

public interface OilPriceClient {

    OilPriceResponse<OilPriceBenchmark> getBenchmark(OilPriceCommodity benchmark);

}
