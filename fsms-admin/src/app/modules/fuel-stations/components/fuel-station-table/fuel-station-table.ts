import { Component, DestroyRef, inject, input, output } from "@angular/core";
import { FuelStation } from "fsms-web-api";
import { ButtonModule } from "primeng/button";
import { SkeletonModule } from "primeng/skeleton";
import { TableModule } from "primeng/table";
import { TagModule } from "primeng/tag";
import { FuelStationTemplate } from "../../directives/fuel-station-template-directive";
import { DeactivateFuelStationHandler } from "../../handlers/deactivate-fuel-station-handler";
import { takeUntilDestroyed, toSignal } from "@angular/core/rxjs-interop";

@Component({
    selector: 'app-fuel-station-table',
    imports: [TableModule, TagModule, SkeletonModule, ButtonModule, FuelStationTemplate],
    templateUrl: './fuel-station-table.html'
})
export class FuelStationTable {
    viewClicked = output<number>();

    fuelStations = input<FuelStation[]>([]);
    loading = input<boolean>(false);

    private readonly destroyRef = inject(DestroyRef);
    private readonly deactivateFuelStationHandler = inject(DeactivateFuelStationHandler);

    protected readonly deactivateLoading = toSignal(
        this.deactivateFuelStationHandler.loading$,
        { initialValue: false }
    );
    protected readonly skeletonRows = new Array(5).fill(null);
    protected readonly skeletonCols = new Array(5).fill(null);

    protected deactivate(fuelStationId: number): void {
        this.deactivateFuelStationHandler
            .handle({ fuelStationId })
            .pipe(takeUntilDestroyed(this.destroyRef))
            .subscribe();
    }

    protected getSeverity(fuelStation: FuelStation): 'success' | undefined {
        return fuelStation.active ? 'success' : undefined;
    }
}
