import { FuelPrice } from './fuel-price-response';
import { FuelTaxRule } from './fuel-tax-rule-response';

export interface TaxedFuelPrice {
    fuelPrice: FuelPrice;
    taxRules: FuelTaxRule[];
}
