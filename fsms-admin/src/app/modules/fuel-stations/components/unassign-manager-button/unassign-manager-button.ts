import { Component, DestroyRef, inject, input } from "@angular/core";
import { takeUntilDestroyed, toSignal } from "@angular/core/rxjs-interop";
import { ButtonModule } from "primeng/button";
import { FuelStationStore } from "../../fuel-station-store";
import { UnassignManagerHandler } from "../../handlers/unassign-manager-handler";

@Component({
    selector: 'app-unassign-manager-button',
    imports: [ButtonModule],
    templateUrl: './unassign-manager-button.html'
})
export class UnassignManagerButton {
    managerId = input.required<number>();

    private readonly destroyRef = inject(DestroyRef);
    private readonly fuelStationStore = inject(FuelStationStore);
    private readonly unassignManagerHandler = inject(UnassignManagerHandler);

    protected readonly loading = toSignal(this.unassignManagerHandler.loading$, { initialValue: false });

    protected onClick() {
        const fuelStationId = this.fuelStationStore.fuelStation.fuelStationId;
        this.unassignManagerHandler
            .handle({ fuelStationId, managerId: this.managerId() })
            .pipe(takeUntilDestroyed(this.destroyRef))
            .subscribe();
    }
}
