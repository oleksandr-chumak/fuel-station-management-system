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

type RangeKey = '24h' | '7d' | '30d' | 'all';
type Bucket = 'none' | 'hour' | 'day';

interface RangeOption {
    label: string;
    value: RangeKey;
}

const RANGE_WINDOWS: Record<RangeKey, { ms: number | null; bucket: Bucket }> = {
    '24h': { ms: 24 * 60 * 60 * 1000, bucket: 'none' },
    '7d':  { ms: 7  * 24 * 60 * 60 * 1000, bucket: 'hour' },
    '30d': { ms: 30 * 24 * 60 * 60 * 1000, bucket: 'day'  },
    'all': { ms: null, bucket: 'none' },
};

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

    protected readonly rangeOptions: RangeOption[] = [
        { label: '24h', value: '24h' },
        { label: '7d',  value: '7d'  },
        { label: '30d', value: '30d' },
        { label: 'All', value: 'all' },
    ];
    protected readonly selectedRange = signal<RangeKey>('7d');

    protected readonly priceHistory = signal<FuelStationFuelPriceHistoryEntry[]>([]);
    protected readonly loadingHistory = toSignal(this.historyHandler.loading$, { initialValue: false });

    protected readonly chartOptions = computed<EChartsCoreOption>(() =>
        this.buildChart(this.priceHistory(), this.selectedRange())
    );

    constructor() {
        effect(() => {
            const id = this.fuelStationId();
            const range = this.selectedRange();
            this.refreshKey();

            const window = RANGE_WINDOWS[range];
            const from = window.ms != null ? new Date(Date.now() - window.ms).toISOString() : undefined;

            this.historyHandler
                .handle({ fuelStationId: id, from })
                .pipe(
                    tap(history => this.priceHistory.set(history)),
                    takeUntilDestroyed(this.destroyRef)
                )
                .subscribe();
        });
    }

    protected onRangeChange(value: RangeKey | null): void {
        if (value != null) this.selectedRange.set(value);
    }

    private buildChart(history: FuelStationFuelPriceHistoryEntry[], range: RangeKey): EChartsCoreOption {
        const timeZone = this.timeZoneForCountry(this.countryCode());
        const currency = history[0]?.currency ?? '';
        const money = new MoneyPipe();
        const nowMs = Date.now();
        const { ms: rangeMs, bucket } = RANGE_WINDOWS[range];
        const fromMs = rangeMs != null ? nowMs - rangeMs : null;

        const gradeConfig = [
            { key: 'ron-92', label: 'RON 92', color: '#3B82F6' },
            { key: 'ron-95', label: 'RON 95', color: '#10B981' },
            { key: 'diesel', label: 'Diesel', color: '#F59E0B' },
        ].filter((grade) => history.some((h) => h.fuelGrade === grade.key));

        const seriesData = new Map<string, [number, number][]>();
        for (const entry of history) {
            const arr = seriesData.get(entry.fuelGrade) ?? [];
            arr.push([new Date(entry.changedAt).getTime(), Number(entry.pricePerLiter)]);
            seriesData.set(entry.fuelGrade, arr);
        }
        for (const arr of seriesData.values()) arr.sort((a, b) => a[0] - b[0]);

        if (bucket !== 'none') {
            for (const [key, points] of seriesData) {
                seriesData.set(key, this.downsampleLastPerBucket(points, bucket));
            }
        }

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
                axisPointer: { type: 'line', snap: true },
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
            legend: { show: true },
            grid: { left: 12, right: 24, top: 28, bottom: 56, containLabel: true },
            dataZoom: [
                { type: 'inside', xAxisIndex: 0, filterMode: 'none', zoomOnMouseWheel: true, moveOnMouseMove: true, moveOnMouseWheel: false },
                { type: 'slider', xAxisIndex: 0, filterMode: 'none', height: 20, bottom: 8, brushSelect: false },
            ],
            xAxis: {
                type: 'time',
                min: fromMs ?? undefined,
                max: nowMs,
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
                    step: 'end' as const,
                    color,
                    showSymbol: false,
                    symbol: 'circle',
                    symbolSize: 6,
                    emphasis: { focus: 'series' },
                    lineStyle: { width: 2 },
                    data,
                };
            }),
        };
    }

    private downsampleLastPerBucket(points: [number, number][], bucket: Bucket): [number, number][] {
        if (bucket === 'none' || points.length === 0) return points;

        const bucketKey = (ts: number): number => {
            const d = new Date(ts);
            if (bucket === 'hour') {
                d.setMinutes(0, 0, 0);
            } else {
                d.setHours(0, 0, 0, 0);
            }
            return d.getTime();
        };

        const lastPerBucket = new Map<number, [number, number]>();
        for (const p of points) {
            lastPerBucket.set(bucketKey(p[0]), p);
        }
        return Array.from(lastPerBucket.values()).sort((a, b) => a[0] - b[0]);
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
