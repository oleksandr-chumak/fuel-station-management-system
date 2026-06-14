import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { toSignal } from '@angular/core/rxjs-interop';
import { FuelStationStore } from '../../../../modules/fuel-stations/stores/fuel-station-store';
import { FuelPriceRecommendation } from './components/fuel-price-recommendation/fuel-price-recommendation';
import { FuelPriceTable } from './components/fuel-price-table/fuel-price-table';
import { FuelPriceHistoryChart } from './components/fuel-price-history-chart/fuel-price-history-chart';

@Component({
    selector: 'app-fuel-station-fuel-prices-page',
    imports: [CommonModule, FuelPriceRecommendation, FuelPriceTable, FuelPriceHistoryChart],
    templateUrl: './fuel-station-fuel-prices.page.html',
})
export class FuelStationFuelPricesPage {
    private readonly fuelStationStore = inject(FuelStationStore);

    protected readonly fuelStation = toSignal(this.fuelStationStore.fuelStation$, {
        initialValue: this.fuelStationStore.fuelStation,
    });

    protected readonly historyRefresh = signal(0);

    protected onPricesChanged(): void {
        this.historyRefresh.update(v => v + 1);
    }
}
