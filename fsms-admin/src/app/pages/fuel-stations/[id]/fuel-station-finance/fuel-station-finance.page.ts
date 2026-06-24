import { Component, computed, DestroyRef, inject, OnDestroy, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PanelModule } from 'primeng/panel';
import { TableModule } from 'primeng/table';
import { SkeletonModule } from 'primeng/skeleton';
import { takeUntilDestroyed, toSignal } from '@angular/core/rxjs-interop';
import { MoneyPipe } from '../../../../modules/common/money.pipe';
import { FuelStationStore } from '../../../../modules/fuel-stations/stores/fuel-station-store';
import { GetFuelStationFuelPurchasesHandler } from '../../../../modules/fuel-stations/handlers/get-fuel-station-fuel-purchases-handler';
import { GetFuelStationFuelSalesHandler } from '../../../../modules/fuel-stations/handlers/get-fuel-station-fuel-sales-handler';

interface GradeProfitRow {
    fuelGrade: string;
    volumeSold: number;
    volumePurchased: number;
    revenue: number;
    cost: number;
    profit: number;
}

interface CurrencyProfitSummary {
    currency: string;
    grades: GradeProfitRow[];
    totalVolumeSold: number;
    totalRevenue: number;
    totalCost: number;
    totalProfit: number;
}

@Component({
    selector: 'app-fuel-station-fuel-purchases-page',
    imports: [CommonModule, PanelModule, TableModule, SkeletonModule, MoneyPipe],
    templateUrl: './fuel-station-finance.page.html'
})
export class FuelStationFinancePage implements OnInit, OnDestroy {
    private readonly destroyRef = inject(DestroyRef);
    private readonly fuelStationStore = inject(FuelStationStore);
    private readonly getPurchasesHandler = inject(GetFuelStationFuelPurchasesHandler);
    private readonly getSalesHandler = inject(GetFuelStationFuelSalesHandler);

    protected readonly purchases = toSignal(this.fuelStationStore.fuelPurchases$, { initialValue: [] });
    protected readonly sales = toSignal(this.fuelStationStore.fuelSales$, { initialValue: [] });
    protected readonly purchasesLoading = toSignal(this.getPurchasesHandler.loading$, { initialValue: false });
    protected readonly salesLoading = toSignal(this.getSalesHandler.loading$, { initialValue: false });

    protected readonly totalSpentByCurrency = computed(() => {
        const map = new Map<string, number>();
        for (const p of this.purchases() ?? []) {
            map.set(p.currency, (map.get(p.currency) ?? 0) + p.totalPrice);
        }
        return Array.from(map.entries()).map(([currency, total]) => ({ currency, total: +total.toFixed(2) }));
    });

    protected readonly totalRevenueByCurrency = computed(() => {
        const map = new Map<string, number>();
        for (const s of this.sales() ?? []) {
            map.set(s.currency, (map.get(s.currency) ?? 0) + s.totalRevenue);
        }
        return Array.from(map.entries()).map(([currency, total]) => ({ currency, total: +total.toFixed(2) }));
    });

    protected readonly profitSummary = computed<CurrencyProfitSummary[]>(() => {
        const sales = this.sales() ?? [];
        const purchases = this.purchases() ?? [];

        const map = new Map<string, Map<string, { volumeSold: number; volumePurchased: number; revenue: number; cost: number }>>();

        const ensureGradeEntry = (currency: string, grade: string) => {
            let byGrade = map.get(currency);
            if (!byGrade) {
                byGrade = new Map();
                map.set(currency, byGrade);
            }
            let entry = byGrade.get(grade);
            if (!entry) {
                entry = { volumeSold: 0, volumePurchased: 0, revenue: 0, cost: 0 };
                byGrade.set(grade, entry);
            }
            return entry;
        };

        for (const s of sales) {
            const entry = ensureGradeEntry(s.currency, s.fuelGrade);
            entry.volumeSold += Number(s.volume);
            entry.revenue += Number(s.totalRevenue);
        }
        for (const p of purchases) {
            const entry = ensureGradeEntry(p.currency, p.fuelGrade);
            entry.volumePurchased += Number(p.amount);
            entry.cost += Number(p.totalPrice);
        }

        return Array.from(map.entries()).map(([currency, byGrade]) => {
            const grades: GradeProfitRow[] = Array.from(byGrade.entries())
                .map(([fuelGrade, agg]) => ({
                    fuelGrade,
                    volumeSold: +agg.volumeSold.toFixed(2),
                    volumePurchased: +agg.volumePurchased.toFixed(2),
                    revenue: +agg.revenue.toFixed(2),
                    cost: +agg.cost.toFixed(2),
                    profit: +(agg.revenue - agg.cost).toFixed(2),
                }))
                .sort((a, b) => a.fuelGrade.localeCompare(b.fuelGrade));

            const totalVolumeSold = grades.reduce((sum, g) => sum + g.volumeSold, 0);
            const totalRevenue = grades.reduce((sum, g) => sum + g.revenue, 0);
            const totalCost = grades.reduce((sum, g) => sum + g.cost, 0);

            return {
                currency,
                grades,
                totalVolumeSold: +totalVolumeSold.toFixed(2),
                totalRevenue: +totalRevenue.toFixed(2),
                totalCost: +totalCost.toFixed(2),
                totalProfit: +(totalRevenue - totalCost).toFixed(2),
            };
        }).sort((a, b) => a.currency.localeCompare(b.currency));
    });

    readonly skeletonRows = new Array(5).fill(null);
    readonly purchasesSkeletonCols = new Array(6).fill(null);
    readonly salesSkeletonCols = new Array(6).fill(null);
    readonly summarySkeletonCols = new Array(5).fill(null);

    ngOnInit(): void {
        const fuelStationId = this.fuelStationStore.fuelStation.fuelStationId;

        this.getPurchasesHandler.handle({ fuelStationId })
            .pipe(takeUntilDestroyed(this.destroyRef))
            .subscribe();

        this.getSalesHandler.handle({ fuelStationId })
            .pipe(takeUntilDestroyed(this.destroyRef))
            .subscribe();
    }

    ngOnDestroy(): void {
        this.fuelStationStore.resetFuelPurchases();
        this.fuelStationStore.resetFuelSales();
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

    protected profitClass(value: number): string {
        if (value > 0) return 'text-green-600 font-semibold';
        if (value < 0) return 'text-red-600 font-semibold';
        return 'font-semibold';
    }
}
