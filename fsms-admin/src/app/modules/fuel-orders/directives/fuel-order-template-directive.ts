import { Directive } from "@angular/core";
import { FuelOrder } from "fsms-web-api";

@Directive({
    selector: 'ng-template[fuelOrderTemplate]'
})
export class FuelTankTemplate {
    static ngTemplateContextGuard(
        _dir: FuelTankTemplate,
        ctx: unknown
    ): ctx is { $implicit: FuelOrder } {
        return true;
    }
}

