import { Component, input, output } from "@angular/core";
import { FuelStation } from "fsms-web-api";
import { ButtonModule } from "primeng/button";
import { SkeletonModule } from "primeng/skeleton";
import { TableModule } from "primeng/table";
import { TagModule } from "primeng/tag";

@Component({
    selector: 'app-fuel-station-table',
    imports: [TableModule, TagModule, SkeletonModule, ButtonModule],
    templateUrl: './fuel-station-table.html'
})
export class FuelStationTable {
    viewClicked = output<number>();

    fuelStations = input<FuelStation[]>([]);
    loading = input<boolean>(false);

    protected readonly skeletonRows = new Array(5).fill(null);
    protected readonly skeletonCols = new Array(5).fill(null);

    protected getSeverity(fuelStation: FuelStation): 'success' | undefined {
        return fuelStation.active ? 'success' : undefined;
    }
}
