import { Directive } from "@angular/core";
import { FuelStation } from "fsms-web-api";

@Directive({
    selector: 'ng-template[fuelStationTemplate]'
})
export class FuelStationTemplate {
    static ngTemplateContextGuard(
        _dir: FuelStationTemplate,
        ctx: unknown
    ): ctx is { $implicit: FuelStation } {
        return true;
    }
}

