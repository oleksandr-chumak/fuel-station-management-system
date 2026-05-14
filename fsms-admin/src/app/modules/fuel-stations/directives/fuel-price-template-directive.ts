import { Directive } from "@angular/core";
import { FuelStationFuelPrice } from "fsms-web-api";

@Directive({
    selector: 'ng-template[fuelPriceTemplate]'
})
export class FuelStationFuelPriceTemplate {
    static ngTemplateContextGuard(
        _dir: FuelStationFuelPriceTemplate,
        ctx: unknown
    ): ctx is { $implicit: FuelStationFuelPrice } {
        return true;
    }
}

