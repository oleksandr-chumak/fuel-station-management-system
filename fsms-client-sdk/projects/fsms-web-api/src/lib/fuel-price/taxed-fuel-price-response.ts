import { FuelPriceResponse } from './fuel-price-response';
import { FuelTaxRuleResponse } from './fuel-tax-rule-response';

export interface TaxedFuelPriceResponse {
    fuelPrice: FuelPriceResponse;
    taxRules: FuelTaxRuleResponse[];
}
