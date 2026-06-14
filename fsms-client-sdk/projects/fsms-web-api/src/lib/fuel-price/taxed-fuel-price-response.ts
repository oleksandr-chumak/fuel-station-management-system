import { TaxRule } from '../tax-rule/tax-rule-response';
import { FuelPrice } from './fuel-price-response';

export interface TaxedFuelPrice {
    fuelPrice: FuelPrice;
    taxRules: TaxRule[];
}
