import { Component, computed, DestroyRef, effect, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { InputTextModule } from 'primeng/inputtext';
import { PanelModule } from 'primeng/panel';
import { SkeletonModule } from 'primeng/skeleton';
import { TableModule } from 'primeng/table';
import { FormsModule } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { MessageModule } from 'primeng/message';
import { MessageService } from 'primeng/api';
import { FuelGrade, FuelStationFuelPrice, TaxedFuelPrice } from 'fsms-web-api';
import { MoneyPipe } from '../../../../modules/common/money.pipe';
import { FuelStationStore } from '../../../../modules/fuel-stations/stores/fuel-station-store';
import { ChangeFuelPriceHandler } from '../../../../modules/fuel-stations/handlers/change-fuel-price-handler';
import { GetLatestTaxedFuelPricesHandler } from '../../../../modules/fuel-prices/handlers/get-latest-taxed-fuel-prices-handler';
import { toSignal, takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { catchError, EMPTY, tap } from 'rxjs';

@Component({
    selector: 'app-fuel-station-fuel-prices-page',
    imports: [CommonModule, TableModule, InputTextModule, SkeletonModule, PanelModule, FormsModule, ButtonModule, MessageModule, MoneyPipe],
    templateUrl: './fuel-station-fuel-prices.page.html'
})
export class FuelStationFuelPricesPage {
    private readonly fuelStationStore = inject(FuelStationStore);
    private readonly changeFuelPriceHandler = inject(ChangeFuelPriceHandler);
    private readonly messageService = inject(MessageService);
    private readonly destroyRef = inject(DestroyRef);
    private readonly getLatestTaxedHandler = inject(GetLatestTaxedFuelPricesHandler);

    private readonly fuelStation = toSignal(this.fuelStationStore.fuelStation$);

    protected readonly fuelPrices = signal<FuelStationFuelPrice[]>(this.fuelStationStore.fuelStation.fuelPrices);
    protected readonly loading = toSignal(this.changeFuelPriceHandler.loading$, { initialValue: false });
    protected readonly recommendedPrices = signal<TaxedFuelPrice[]>([]);
    protected readonly loadingRecommended = toSignal(this.getLatestTaxedHandler.loading$, { initialValue: false });
    protected readonly countryCode = this.fuelStationStore.fuelStation.country;

    protected readonly recommendedRows = computed(() =>
        this.recommendedPrices().map(item => ({
            fuelGrade: item.fuelPrice.fuelGrade,
            marketPrice: item.fuelPrice.price,
            recommendedPrice: +(item.fuelPrice.price * 1.10).toFixed(2),
            currency: item.fuelPrice.currency,
        }))
    );

    constructor() {
        effect(() => {
            const station = this.fuelStation();
            if (station) {
                this.fuelPrices.set(station.fuelPrices);
            }
        });

        this.getLatestTaxedHandler.handle({ countryCode: this.fuelStationStore.fuelStation.country })
            .pipe(
                tap(prices => this.recommendedPrices.set(prices)),
                takeUntilDestroyed(this.destroyRef)
            )
            .subscribe();
    }

    readonly skeletonRows = new Array(5).fill(null);
    readonly skeletonCols = new Array(3).fill(null);

    getFuelGradeValue(fuelGrade: FuelGrade): string {
        return FuelGrade[fuelGrade];
    }

    protected formatGrade(grade: string): string {
        return grade.replace(/_/g, ' ').replace(/\b\w/g, c => c.toUpperCase());
    }

    onRowEditInit(): void {
        this.resetFuelPrices();
    }

    onRowEditSave(fuelPrice: FuelStationFuelPrice): void {
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