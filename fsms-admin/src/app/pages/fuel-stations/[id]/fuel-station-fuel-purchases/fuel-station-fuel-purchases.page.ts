import { Component, computed, DestroyRef, inject, OnDestroy, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PanelModule } from 'primeng/panel';
import { TableModule } from 'primeng/table';
import { SkeletonModule } from 'primeng/skeleton';
import { takeUntilDestroyed, toSignal } from '@angular/core/rxjs-interop';
import { MoneyPipe } from '../../../../modules/common/money.pipe';
import { FuelStationStore } from '../../../../modules/fuel-stations/stores/fuel-station-store';
import { GetFuelStationFuelPurchasesHandler } from '../../../../modules/fuel-stations/handlers/get-fuel-station-fuel-purchases-handler';

@Component({
    selector: 'app-fuel-station-fuel-purchases-page',
    imports: [CommonModule, PanelModule, TableModule, SkeletonModule, MoneyPipe],
    templateUrl: './fuel-station-fuel-purchases.page.html'
})
export class FuelStationFuelPurchasesPage implements OnInit, OnDestroy {
    private readonly destroyRef = inject(DestroyRef);
    private readonly fuelStationStore = inject(FuelStationStore);
    private readonly getHandler = inject(GetFuelStationFuelPurchasesHandler);

    protected readonly purchases = toSignal(this.fuelStationStore.fuelPurchases$, { initialValue: [] });
    protected readonly loading = toSignal(this.getHandler.loading$, { initialValue: false });

    protected readonly totalByCurrency = computed(() => {
        const map = new Map<string, number>();
        for (const p of this.purchases() ?? []) {
            map.set(p.currency, (map.get(p.currency) ?? 0) + p.totalPrice);
        }
        return Array.from(map.entries()).map(([currency, total]) => ({ currency, total: +total.toFixed(2) }));
    });

    readonly skeletonRows = new Array(5).fill(null);
    readonly skeletonCols = new Array(6).fill(null);

    ngOnInit(): void {
        this.getHandler.handle({ fuelStationId: this.fuelStationStore.fuelStation.fuelStationId })
            .pipe(takeUntilDestroyed(this.destroyRef))
            .subscribe();
    }

    ngOnDestroy(): void {
        this.fuelStationStore.resetFuelPurchases();
    }

    protected formatGrade(grade: string): string {
        return grade.replace(/_/g, ' ').replace(/\b\w/g, c => c.toUpperCase());
    }

    protected formatDate(iso: string): string {
        return new Date(iso).toLocaleString('en-GB', {
            day: '2-digit', month: 'short', year: 'numeric',
            hour: '2-digit', minute: '2-digit'
        });
    }
}
