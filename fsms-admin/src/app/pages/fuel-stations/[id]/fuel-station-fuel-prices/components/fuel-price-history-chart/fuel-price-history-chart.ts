import { Component, DestroyRef, computed, effect, inject, input, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { PanelModule } from 'primeng/panel';
import { SkeletonModule } from 'primeng/skeleton';
import { SelectButtonModule } from 'primeng/selectbutton';
import { NgxEchartsDirective } from 'ngx-echarts';
import type { EChartsCoreOption } from 'echarts/core';
import { takeUntilDestroyed, toSignal } from '@angular/core/rxjs-interop';
import { tap } from 'rxjs';
import { FuelStationFuelPriceHistoryEntry } from 'fsms-web-api';
import { MoneyPipe } from '../../../../../../modules/common/money.pipe';
import { GetFuelPriceHistoryHandler } from '../../../../../../modules/fuel-stations/handlers/get-fuel-price-history-handler';

@Component({
    selector: 'app-fuel-price-history-chart',
    imports: [CommonModule, FormsModule, PanelModule, SkeletonModule, SelectButtonModule, NgxEchartsDirective],
    templateUrl: './fuel-price-history-chart.html',
})
export class FuelPriceHistoryChart {
    fuelStationId = input.required<number>();
    countryCode = input.required<string>();
    refreshKey = input<number>(0);

    private readonly destroyRef = inject(DestroyRef);
    private readonly historyHandler = inject(GetFuelPriceHistoryHandler);

    protected readonly priceHistory = signal<FuelStationFuelPriceHistoryEntry[]>([]);
    protected readonly loadingHistory = toSignal(this.historyHandler.loading$, { initialValue: false });
    protected selectedGrades = signal<string[]>(['ron-92', 'ron-95', 'diesel']);

    protected readonly gradeOptions = [
        { label: 'RON 92', value: 'ron-92' },
        { label: 'RON 95', value: 'ron-95' },
        { label: 'Diesel', value: 'diesel' },
    ];

    protected readonly chartOptions = computed<EChartsCoreOption>(() =>
        this.buildChart(this.priceHistory(), this.selectedGrades())
    );

    constructor() {
        effect(() => {
            const id = this.fuelStationId();
            this.refreshKey();

            this.historyHandler
                .handle({ fuelStationId: id })
                .pipe(
                    tap(history => this.priceHistory.set(history)),
                    takeUntilDestroyed(this.destroyRef)
                )
                .subscribe();
        });
    }

    private buildChart(history: FuelStationFuelPriceHistoryEntry[], selectedGrades: string[]): EChartsCoreOption {
        const timeZone = this.timeZoneForCountry(this.countryCode());
        const currency = history[0]?.currency ?? '';
        const money = new MoneyPipe();
        const nowMs = Date.now();

        const gradeConfig = [
            { key: 'ron-92', label: 'RON 92', color: '#3B82F6' },
            { key: 'ron-95', label: 'RON 95', color: '#10B981' },
            { key: 'diesel', label: 'Diesel', color: '#F59E0B' },
        ].filter(g => selectedGrades.includes(g.key));

        const seriesData = new Map<string, [number, number][]>();
        for (const entry of history) {
            const arr = seriesData.get(entry.fuelGrade) ?? [];
            arr.push([new Date(entry.changedAt).getTime(), Number(entry.pricePerLiter)]);
            seriesData.set(entry.fuelGrade, arr);
        }
        for (const arr of seriesData.values()) arr.sort((a, b) => a[0] - b[0]);

        const tzParts = (ts: number) => {
            const parts = new Intl.DateTimeFormat('en-GB', {
                timeZone, hour12: false,
                year: 'numeric', month: 'short', day: '2-digit',
                hour: '2-digit', minute: '2-digit', second: '2-digit',
            }).formatToParts(new Date(ts));
            const get = (t: string) => parts.find(p => p.type === t)?.value ?? '';
            return {
                day: get('day'), month: get('month'), year: get('year'),
                hour: get('hour'), minute: get('minute'), second: get('second'),
            };
        };

        const formatAxisLabel = (value: number): string => {
            const p = tzParts(value);
            if (p.hour === '00' && p.minute === '00' && p.second === '00') {
                return `${p.day} ${p.month}`;
            }
            if (p.second === '00') {
                return `${p.hour}:${p.minute}`;
            }
            return `${p.hour}:${p.minute}:${p.second}`;
        };

        const formatTooltipTime = (ts: number): string => {
            const p = tzParts(ts);
            return `${p.day} ${p.month} ${p.year}, ${p.hour}:${p.minute}:${p.second}`;
        };

        return {
            tooltip: {
                trigger: 'axis',
                axisPointer: { type: 'line' },
                formatter: (params: any) => {
                    const arr = params as any[];
                    if (!arr.length) return '';
                    const ts = Array.isArray(arr[0].value) ? arr[0].value[0] : arr[0].axisValue;
                    const header = `<div style="margin-bottom:4px;font-weight:600;">${formatTooltipTime(ts)}</div>`;
                    const lines = arr
                        .filter(p => Array.isArray(p.value) && p.value[1] != null)
                        .map(p => `${p.marker}${p.seriesName}: ${money.transform(Number(p.value[1]), currency)}`);
                    return header + lines.join('<br/>');
                },
            },
            legend: { show: false },
            grid: { left: 12, right: 24, top: 28, bottom: 12, containLabel: true },
            xAxis: {
                type: 'time',
                axisLabel: {
                    formatter: formatAxisLabel,
                    hideOverlap: true,
                },
            },
            yAxis: {
                type: 'value',
                scale: true,
                name: currency,
                axisLabel: { formatter: (v: number) => v.toFixed(2) },
            },
            series: gradeConfig.map(({ key, label, color }) => {
                const points = seriesData.get(key) ?? [];
                const data: any[] = points.map(([t, v]) => ({ value: [t, v] }));
                if (points.length > 0) {
                    const last = points[points.length - 1];
                    if (nowMs > last[0]) {
                        data.push({ value: [nowMs, last[1]], symbol: 'none' });
                    }
                }
                return {
                    name: label,
                    type: 'line',
                    smooth: true,
                    color,
                    showSymbol: true,
                    symbolSize: 6,
                    data,
                };
            }),
        };
    }

    private timeZoneForCountry(country: string): string {
        switch (String(country).toUpperCase()) {
            case 'UA': return 'Europe/Kyiv';
            case 'DE': return 'Europe/Berlin';
            case 'NO': return 'Europe/Oslo';
            default: return 'UTC';
        }
    }
}
