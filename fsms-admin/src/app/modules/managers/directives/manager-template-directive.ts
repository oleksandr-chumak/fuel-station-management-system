import { Directive } from "@angular/core";
import { Manager } from "fsms-web-api";

@Directive({
    selector: 'ng-template[managerTemplate]'
})
export class ManagerTemplateDirective {
    static ngTemplateContextGuard(
        _dir: ManagerTemplateDirective,
        ctx: unknown
    ): ctx is { $implicit: Manager } {
        return true;
    }
}
