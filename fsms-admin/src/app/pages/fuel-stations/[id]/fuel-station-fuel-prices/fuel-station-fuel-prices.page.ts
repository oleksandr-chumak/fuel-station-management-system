import { Component, effect, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { InputTextModule } from 'primeng/inputtext';
import { PanelModule } from 'primeng/panel';
import { SkeletonModule } from 'primeng/skeleton';
import { TableModule } from 'primeng/table';
import { FormsModule } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { MessageService } from 'primeng/api';
import { FuelGrade, FuelPrice } from 'fsms-web-api';
import { FuelStationStore } from '../../../../modules/fuel-stations/fuel-station-store';
import { ChangeFuelPriceHandler } from '../../../../modules/fuel-stations/handlers/change-fuel-price-handler';
import { toSignal } from '@angular/core/rxjs-interop';
import { catchError, EMPTY } from 'rxjs';

@Component({
    selector: 'app-fuel-station-fuel-prices-page',
    imports: [CommonModule, TableModule, InputTextModule, SkeletonModule, PanelModule, FormsModule, ButtonModule],
    templateUrl: './fuel-station-fuel-prices.page.html'
})
export class FuelStationFuelPricesPage {
    private readonly fuelStationStore = inject(FuelStationStore);
    private readonly changeFuelPriceHandler = inject(ChangeFuelPriceHandler);
    private readonly messageService = inject(MessageService);

    private readonly fuelStation = toSignal(this.fuelStationStore.fuelStation$);

    protected readonly fuelPrices = signal<FuelPrice[]>(this.fuelStationStore.fuelStation.fuelPrices);
    protected readonly loading = toSignal(this.changeFuelPriceHandler.loading$, { initialValue: false });

    constructor() {
        effect(() => {
            const station = this.fuelStation();
            if (station) {
                this.fuelPrices.set(station.fuelPrices);
            }
        });
    }

    readonly skeletonRows = new Array(5).fill(null);
    readonly skeletonCols = new Array(3).fill(null);

    getFuelGradeValue(fuelGrade: FuelGrade): string {
        return FuelGrade[fuelGrade];
    }

    onRowEditInit(): void {
        this.resetFuelPrices();
    }

    onRowEditSave(fuelPrice: FuelPrice): void {
        const newFuelPrice = Number(fuelPrice.pricePerLiter);

        if (Number.isNaN(newFuelPrice)) {
            this.resetFuelPrices();
            this.messageService.add({ 
                severity: 'error', 
                summary: 'Validation', 
                detail: 'Fuel price must be a number' 
            });
            return;
        }

        this.changeFuelPriceHandler
            .handle({
                fuelStationId: this.fuelStation()!.fuelStationId,
                fuelGrade: fuelPrice.fuelGrade,
                newPrice: newFuelPrice
            })
            .pipe(
                catchError(() => {
                    this.resetFuelPrices();
                    return EMPTY;
                })
            )
            .subscribe();
    }

    onRowEditCancel(): void {
        this.resetFuelPrices();
    }

    private resetFuelPrices(): void {
        this.fuelPrices.set(this.fuelStation()!.clone().fuelPrices);
    }
}