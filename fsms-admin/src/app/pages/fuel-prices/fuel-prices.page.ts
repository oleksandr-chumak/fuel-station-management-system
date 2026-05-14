import { Component, computed, DestroyRef, inject, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { PanelModule } from 'primeng/panel';
import { TableModule } from 'primeng/table';
import { SkeletonModule } from 'primeng/skeleton';
import { SelectButtonModule } from 'primeng/selectbutton';
import { takeUntilDestroyed, toSignal } from '@angular/core/rxjs-interop';
import { tap } from 'rxjs';
import { NgxEchartsDirective } from 'ngx-echarts';
import type { EChartsCoreOption } from 'echarts/core';
import { FuelPriceResponse, TaxedFuelPriceResponse } from 'fsms-web-api';
import { GetLatestFuelPricesHandler } from '../../modules/fuel-prices/handlers/get-latest-fuel-prices-handler';
import { GetAllFuelPricesHandler } from '../../modules/fuel-prices/handlers/get-all-fuel-prices-handler';
import { GetAllTaxedFuelPricesHandler } from '../../modules/fuel-prices/handlers/get-all-taxed-fuel-prices-handler';

@Component({
    selector: 'app-fuel-prices-page',
    standalone: true,
    imports: [CommonModule, FormsModule, PanelModule, TableModule, SkeletonModule, SelectButtonModule, NgxEchartsDirective],
    templateUrl: './fuel-prices.page.html',
})
export class FuelPricesPage implements OnInit {
    private readonly destroyRef = inject(DestroyRef);
    private readonly getLatestHandler = inject(GetLatestFuelPricesHandler);
    private readonly getAllHandler = inject(GetAllFuelPricesHandler);
    private readonly getTaxedHandler = inject(GetAllTaxedFuelPricesHandler);

    protected readonly latestPrices = signal<FuelPriceResponse[]>([]);
    protected readonly priceHistory = signal<FuelPriceResponse[]>([]);
    protected readonly taxedPricesCache = signal<Record<string, TaxedFuelPriceResponse[]>>({});
    protected readonly selectedTab = signal<string>('no-tax');

    protected readonly loadingLatest = toSignal(this.getLatestHandler.loading$, { initialValue: false });
    protected readonly loadingHistory = toSignal(this.getAllHandler.loading$, { initialValue: false });
    protected readonly loadingTaxed = toSignal(this.getTaxedHandler.loading$, { initialValue: false });

    protected readonly chartTabs = [
        { label: 'Without Taxes', value: 'no-tax' },
        { label: 'Ukraine', value: 'UA' },
        { label: 'Norway', value: 'NO' },
        { label: 'Germany', value: 'DE' },
    ];

    protected readonly chartOptions = computed<EChartsCoreOption>(() => {
        const tab = this.selectedTab();
        if (tab === 'no-tax') return this.buildLineChartOptions(this.priceHistory());
        const taxedHistory = (this.taxedPricesCache()[tab] ?? []).map(t => t.fuelPrice);
        return this.buildLineChartOptions(taxedHistory, taxedHistory[0]?.currency);
    });

    protected readonly displayedLatestPrices = computed<FuelPriceResponse[]>(() => {
        const tab = this.selectedTab();
        if (tab === 'no-tax') return this.latestPrices();
        const cached = this.taxedPricesCache()[tab] ?? [];
        const latestByGrade = new Map<string, FuelPriceResponse>();
        for (const item of cached) {
            const existing = latestByGrade.get(item.fuelPrice.fuelGrade);
            if (!existing || item.fuelPrice.fetchedAt > existing.fetchedAt) {
                latestByGrade.set(item.fuelPrice.fuelGrade, item.fuelPrice);
            }
        }
        return Array.from(latestByGrade.values());
    });

    protected readonly loadingTable = computed(() =>
        this.selectedTab() === 'no-tax' ? this.loadingLatest() : this.loadingTaxed()
    );

    protected readonly skeletonRows = new Array(3).fill(null);
    protected readonly skeletonCols = new Array(3).fill(null);

    ngOnInit(): void {
        this.getLatestHandler
            .handle({})
            .pipe(
                tap(prices => this.latestPrices.set(prices)),
                takeUntilDestroyed(this.destroyRef)
            )
            .subscribe();

        this.getAllHandler
            .handle({})
            .pipe(
                tap(history => this.priceHistory.set(history)),
                takeUntilDestroyed(this.destroyRef)
            )
            .subscribe();
    }

    protected onTabChange(tab: string): void {
        this.selectedTab.set(tab);
        if (tab === 'no-tax' || this.taxedPricesCache()[tab]) return;
        this.getTaxedHandler.handle({ countryCode: tab }).pipe(
            tap(prices => this.taxedPricesCache.update(cache => ({ ...cache, [tab]: prices }))),
            takeUntilDestroyed(this.destroyRef)
        ).subscribe();
    }

    protected formatGrade(grade: string): string {
        switch (grade) {
            case 'ron-92': return 'RON 92';
            case 'ron-95': return 'RON 95';
            case 'diesel': return 'Diesel';
            default: return grade;
        }
    }

    protected formatDate(iso: string): string {
        return new Date(iso).toLocaleString('en-GB', {
            day: '2-digit', month: 'short', year: 'numeric',
            hour: '2-digit', minute: '2-digit'
        });
    }

    protected formatPrice(price: number, currency: string): string {
        return `${Number(price).toFixed(2)} ${this.currencySymbol(currency)}`;
    }

    private currencySymbol(currency: string): string {
        const symbols: Record<string, string> = { USD: '$', EUR: '€', NOK: 'kr', UAH: '₴' };
        return symbols[currency] ?? currency;
    }

    private buildLineChartOptions(history: FuelPriceResponse[], currency?: string): EChartsCoreOption {
        const toDateLabel = (ts: string) =>
            new Date(ts).toLocaleDateString('en-GB', { day: '2-digit', month: 'short', year: 'numeric' });

        const sorted = [...history].sort((a, b) => a.fetchedAt.localeCompare(b.fetchedAt));

        const dateLabels: string[] = [];
        const seenDates = new Set<string>();
        for (const entry of sorted) {
            const d = toDateLabel(entry.fetchedAt);
            if (!seenDates.has(d)) { seenDates.add(d); dateLabels.push(d); }
        }

        const byGrade = new Map<string, Map<string, number>>();
        for (const entry of sorted) {
            if (!byGrade.has(entry.fuelGrade)) byGrade.set(entry.fuelGrade, new Map());
            byGrade.get(entry.fuelGrade)!.set(toDateLabel(entry.fetchedAt), Number(entry.price));
        }

        const gradeConfig = [
            { key: 'ron-92', label: 'RON 92', color: '#3B82F6' },
            { key: 'ron-95', label: 'RON 95', color: '#10B981' },
            { key: 'diesel', label: 'Diesel', color: '#F59E0B' },
        ];

        const sym = currency ? this.currencySymbol(currency) : '';
        return {
            tooltip: {
                trigger: 'axis',
                formatter: (params: any) =>
                    (params as any[]).filter(p => p.value != null)
                        .map(p => `${p.marker}${p.seriesName}: ${Number(p.value).toFixed(2)} ${sym}`)
                        .join('<br/>'),
            },
            legend: { data: gradeConfig.map(g => g.label) },
            grid: { left: '3%', right: '4%', bottom: '3%', containLabel: true },
            xAxis: { type: 'category', boundaryGap: false, data: dateLabels },
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
                data: dateLabels.map(d => byGrade.get(key)?.get(d) ?? null),
            })),
        };
    }
}
