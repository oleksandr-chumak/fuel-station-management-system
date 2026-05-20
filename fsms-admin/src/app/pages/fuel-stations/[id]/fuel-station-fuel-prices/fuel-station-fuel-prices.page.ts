import { Component, computed, DestroyRef, effect, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { InputTextModule } from 'primeng/inputtext';
import { PanelModule } from 'primeng/panel';
import { SkeletonModule } from 'primeng/skeleton';
import { TableModule } from 'primeng/table';
import { FormsModule } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { MessageModule } from 'primeng/message';
import { SelectButtonModule } from 'primeng/selectbutton';
import { MessageService } from 'primeng/api';
import { NgxEchartsDirective } from 'ngx-echarts';
import type { EChartsCoreOption } from 'echarts/core';
import { FuelGrade, FuelStationFuelPrice, FuelStationFuelPriceHistoryEntry, TaxedFuelPrice } from 'fsms-web-api';
import { MoneyPipe } from '../../../../modules/common/money.pipe';
import { FuelStationStore } from '../../../../modules/fuel-stations/stores/fuel-station-store';
import { ChangeFuelPriceHandler } from '../../../../modules/fuel-stations/handlers/change-fuel-price-handler';
import { GetLatestTaxedFuelPricesHandler } from '../../../../modules/fuel-prices/handlers/get-latest-taxed-fuel-prices-handler';
import { GetFuelPriceHistoryHandler } from '../../../../modules/fuel-stations/handlers/get-fuel-price-history-handler';
import { toSignal, takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { catchError, EMPTY, tap } from 'rxjs';

@Component({
    selector: 'app-fuel-station-fuel-prices-page',
    imports: [CommonModule, TableModule, InputTextModule, SkeletonModule, PanelModule, FormsModule, ButtonModule, MessageModule, SelectButtonModule, NgxEchartsDirective, MoneyPipe],
    templateUrl: './fuel-station-fuel-prices.page.html'
})
export class FuelStationFuelPricesPage {
    private readonly fuelStationStore = inject(FuelStationStore);
    private readonly changeFuelPriceHandler = inject(ChangeFuelPriceHandler);
    private readonly messageService = inject(MessageService);
    private readonly destroyRef = inject(DestroyRef);
    private readonly getLatestTaxedHandler = inject(GetLatestTaxedFuelPricesHandler);
    private readonly historyHandler = inject(GetFuelPriceHistoryHandler);

    private readonly fuelStation = toSignal(this.fuelStationStore.fuelStation$);

    protected readonly fuelPrices = signal<FuelStationFuelPrice[]>(this.fuelStationStore.fuelStation.fuelPrices);
    protected readonly loading = toSignal(this.changeFuelPriceHandler.loading$, { initialValue: false });
    protected readonly recommendedPrices = signal<TaxedFuelPrice[]>([]);
    protected readonly loadingRecommended = toSignal(this.getLatestTaxedHandler.loading$, { initialValue: false });
    protected readonly countryCode = this.fuelStationStore.fuelStation.country;

    protected readonly priceHistory = signal<FuelStationFuelPriceHistoryEntry[]>([]);
    protected readonly loadingHistory = toSignal(this.historyHandler.loading$, { initialValue: false });
    protected selectedGrades = signal<string[]>(['ron-92', 'ron-95', 'diesel']);

    protected readonly gradeOptions = [
        { label: 'RON 92', value: 'ron-92' },
        { label: 'RON 95', value: 'ron-95' },
        { label: 'Diesel', value: 'diesel' },
    ];

    protected readonly recommendedRows = computed(() =>
        this.recommendedPrices().map(item => ({
            fuelGrade: item.fuelPrice.fuelGrade,
            marketPrice: item.fuelPrice.price,
            recommendedPrice: +(item.fuelPrice.price * 1.10).toFixed(2),
            currency: item.fuelPrice.currency,
        }))
    );

    protected readonly historyChartOptions = computed<EChartsCoreOption>(() =>
        this.buildHistoryChart(this.priceHistory(), this.selectedGrades())
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

        this.historyHandler.handle({ fuelStationId: this.fuelStationStore.fuelStation.fuelStationId! })
            .pipe(
                tap(history => this.priceHistory.set(history)),
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
                tap(() => this.reloadHistory()),
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

    private reloadHistory(): void {
        this.historyHandler.handle({ fuelStationId: this.fuelStationStore.fuelStation.fuelStationId! })
            .pipe(
                tap(history => this.priceHistory.set(history)),
                takeUntilDestroyed(this.destroyRef)
            )
            .subscribe();
    }

    private buildHistoryChart(history: FuelStationFuelPriceHistoryEntry[], selectedGrades: string[]): EChartsCoreOption {
        const toDisplayLabel = (ts: string) =>
            new Date(ts).toLocaleString('en-GB', {
                day: '2-digit', month: 'short', year: 'numeric',
                hour: '2-digit', minute: '2-digit', second: '2-digit'
            });

        const sorted = [...history].sort((a, b) => a.changedAt.localeCompare(b.changedAt));

        // Use raw changedAt ISO string as key to guarantee uniqueness across events
        const timeKeys = [...new Set(sorted.map(e => e.changedAt))];

        const byGrade = new Map<string, Map<string, number>>();
        for (const entry of sorted) {
            if (!byGrade.has(entry.fuelGrade)) byGrade.set(entry.fuelGrade, new Map());
            byGrade.get(entry.fuelGrade)!.set(entry.changedAt, Number(entry.pricePerLiter));
        }

        const currency = history[0]?.currency ?? '';

        const gradeConfig = [
            { key: 'ron-92', label: 'RON 92', color: '#3B82F6' },
            { key: 'ron-95', label: 'RON 95', color: '#10B981' },
            { key: 'diesel', label: 'Diesel', color: '#F59E0B' },
        ].filter(g => selectedGrades.includes(g.key));

        const money = new MoneyPipe();
        return {
            tooltip: {
                trigger: 'axis',
                formatter: (params: any) =>
                    (params as any[]).filter(p => p.value != null)
                        .map(p => `${p.marker}${p.seriesName}: ${money.transform(Number(p.value), currency)}`)
                        .join('<br/>'),
            },
            legend: { data: gradeConfig.map(g => g.label) },
            grid: { left: '3%', right: '4%', bottom: '3%', containLabel: true },
            xAxis: { type: 'category', boundaryGap: false, data: timeKeys.map(toDisplayLabel) },
            yAxis: {
                type: 'value',
                scale: true,
                name: currency,
                axisLabel: { formatter: (v: number) => v.toFixed(2) },
            },
            series: gradeConfig.map(({ key, label, color }) => ({
                name: label,
                type: 'line',
                smooth: true,
                color,
                connectNulls: true,
                data: timeKeys.map(t => byGrade.get(key)?.get(t) ?? null),
            })),
        };
    }
}