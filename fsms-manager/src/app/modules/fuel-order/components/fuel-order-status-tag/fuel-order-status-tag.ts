import { Component, computed, input } from "@angular/core";
import { TagModule } from "primeng/tag";
import { FuelOrderStatus } from "fsms-web-api";
import { TranslatePipe } from "@ngx-translate/core";

@Component({
    selector: 'app-fuel-order-status-tag',
    standalone: true,
    imports: [TagModule, TranslatePipe],
    template: `<p-tag [value]="label() | translate" [severity]="severity()" />`,
})
export class FuelOrderStatusTag {
    status = input.required<FuelOrderStatus>();

    protected readonly label = computed(() => {
        switch (this.status()) {
            case FuelOrderStatus.Pending:   return 'fuelOrders.statusPending';
            case FuelOrderStatus.Confirmed: return 'fuelOrders.statusConfirmed';
            case FuelOrderStatus.Rejected:  return 'fuelOrders.statusRejected';
            case FuelOrderStatus.Processed: return 'fuelOrders.statusProcessed';
            default:                        return '';
        }
    });

    protected readonly severity = computed<'success' | 'info' | 'danger' | 'warn' | undefined>(() => {
        switch (this.status()) {
            case FuelOrderStatus.Pending:   return 'info';
            case FuelOrderStatus.Confirmed: return 'success';
            case FuelOrderStatus.Rejected:  return 'danger';
            case FuelOrderStatus.Processed: return 'success';
            default:                        return undefined;
        }
    });
}
