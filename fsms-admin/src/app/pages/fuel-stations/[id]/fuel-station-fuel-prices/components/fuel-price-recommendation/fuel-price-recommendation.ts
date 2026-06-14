import { Component, DestroyRef, OnInit, computed, inject, input, output, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PanelModule } from 'primeng/panel';
import { TableModule } from 'primeng/table';
import { SkeletonModule } from 'primeng/skeleton';
import { ButtonModule } from 'primeng/button';
import { takeUntilDestroyed, toSignal } from '@angular/core/rxjs-interop';
import { catchError, EMPTY, tap } from 'rxjs';
import { FuelGrade, TaxedFuelPrice } from 'fsms-web-api';
import { MoneyPipe } from '../../../../../../modules/common/money.pipe';
import { GetLatestTaxedFuelPricesHandler } from '../../../../../../modules/fuel-prices/handlers/get-latest-taxed-fuel-prices-handler';
import { UpdateFuelPricesHandler } from '../../../../../../modules/fuel-stations/handlers/update-fuel-prices-handler';

@Component({
    selector: 'app-fuel-price-recommendation',
    imports: [CommonModule, PanelModule, TableModule, SkeletonModule, ButtonModule, MoneyPipe],
    templateUrl: './fuel-price-recommendation.html',
})
export class FuelPriceRecommendation implements OnInit {
    fuelStationId = input.required<number>();
    countryCode = input.required<string>();
    applied = output<void>();

    private readonly destroyRef = inject(DestroyRef);
    private readonly getLatestTaxedHandler = inject(GetLatestTaxedFuelPricesHandler);
    private readonly updateFuelPricesHandler = inject(UpdateFuelPricesHandler);

    protected readonly recommendedPrices = signal<TaxedFuelPrice[]>([]);
    protected readonly loadingRecommended = toSignal(this.getLatestTaxedHandler.loading$, { initialValue: false });
    protected readonly applyingRecommended = toSignal(this.updateFuelPricesHandler.loading$, { initialValue: false });

    protected readonly recommendedRows = computed(() =>
        this.recommendedPrices().map(item => ({
            fuelGrade: item.fuelPrice.fuelGrade,
            marketPrice: item.fuelPrice.price,
            recommendedPrice: +(item.fuelPrice.price * 1.10).toFixed(2),
            currency: item.fuelPrice.currency,
        }))
    );

    ngOnInit(): void {
        this.getLatestTaxedHandler
            .handle({ countryCode: this.countryCode() })
            .pipe(
                tap(prices => this.recommendedPrices.set(prices)),
                takeUntilDestroyed(this.destroyRef)
            )
            .subscribe();
    }

    protected formatGrade(grade: string): string {
        return grade.replace(/_/g, ' ').replace(/\b\w/g, c => c.toUpperCase());
    }

    protected onApplyRecommendedPrices(): void {
        const rows = this.recommendedRows();
        if (rows.length === 0 || this.applyingRecommended()) {
            return;
        }

        this.updateFuelPricesHandler
            .handle({
                fuelStationId: this.fuelStationId(),
                prices: rows.map(row => ({
                    fuelGrade: this.fuelGradeFromKey(row.fuelGrade),
                    newPrice: row.recommendedPrice,
                })),
            })
            .pipe(
                tap(() => this.applied.emit()),
                catchError(() => EMPTY),
                takeUntilDestroyed(this.destroyRef),
            )
            .subscribe();
    }

    private fuelGradeFromKey(key: string): FuelGrade {
        switch (key) {
            case 'ron-92': return FuelGrade.RON_92;
            case 'ron-95': return FuelGrade.RON_95;
            case 'diesel': return FuelGrade.Diesel;
            default: throw new Error(`Unknown fuel grade key: ${key}`);
        }
    }
}
