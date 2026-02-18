import { Directive } from "@angular/core";
import { FuelPrice } from "fsms-web-api";

@Directive({
    selector: 'ng-template[fuelPriceTemplate]'
})
export class FuelPriceTemplate {
    static ngTemplateContextGuard(
        _dir: FuelPriceTemplate,
        ctx: unknown
    ): ctx is { $implicit: FuelPrice } {
        return true;
    }
}

