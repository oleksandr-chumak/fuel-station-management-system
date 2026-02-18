import { Directive } from "@angular/core";
import { FuelTank } from "fsms-web-api";

@Directive({
    selector: 'ng-template[fuelTankTemplate]'
})
export class FuelTankTemplate {
    static ngTemplateContextGuard(
        _dir: FuelTankTemplate,
        ctx: unknown
    ): ctx is { $implicit: FuelTank } {
        return true;
    }
}

