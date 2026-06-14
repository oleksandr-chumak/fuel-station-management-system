import { Component, effect, inject, input, output, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { InputTextModule } from 'primeng/inputtext';
import { PanelModule } from 'primeng/panel';
import { SkeletonModule } from 'primeng/skeleton';
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { MessageService } from 'primeng/api';
import { toSignal } from '@angular/core/rxjs-interop';
import { catchError, EMPTY, tap } from 'rxjs';
import { FuelGrade, FuelStation, FuelStationFuelPrice } from 'fsms-web-api';
import { MoneyPipe } from '../../../../../../modules/common/money.pipe';
import { ChangeFuelPriceHandler } from '../../../../../../modules/fuel-stations/handlers/change-fuel-price-handler';

@Component({
    selector: 'app-fuel-price-table',
    imports: [CommonModule, FormsModule, InputTextModule, PanelModule, SkeletonModule, TableModule, ButtonModule, MoneyPipe],
    templateUrl: './fuel-price-table.html',
})
export class FuelPriceTable {
    fuelStation = input.required<FuelStation>();
    changed = output<void>();

    private readonly changeFuelPriceHandler = inject(ChangeFuelPriceHandler);
    private readonly messageService = inject(MessageService);

    protected readonly fuelPrices = signal<FuelStationFuelPrice[]>([]);
    protected readonly loading = toSignal(this.changeFuelPriceHandler.loading$, { initialValue: false });

    protected readonly skeletonRows = new Array(5).fill(null);
    protected readonly skeletonCols = new Array(3).fill(null);

    constructor() {
        effect(() => {
            this.fuelPrices.set(this.fuelStation().fuelPrices);
        });
    }

    protected getFuelGradeValue(fuelGrade: FuelGrade): string {
        return FuelGrade[fuelGrade];
    }

    protected onRowEditInit(): void {
        this.resetFuelPrices();
    }

    protected onRowEditSave(fuelPrice: FuelStationFuelPrice): void {
        const newFuelPrice = Number(fuelPrice.pricePerLiter);

        if (Number.isNaN(newFuelPrice)) {
            this.resetFuelPrices();
            this.messageService.add({
                severity: 'error',
                summary: 'Validation',
                detail: 'Fuel price must be a number',
            });
            return;
        }

        this.changeFuelPriceHandler
            .handle({
                fuelStationId: this.fuelStation().fuelStationId,
                fuelGrade: fuelPrice.fuelGrade,
                newPrice: newFuelPrice,
            })
            .pipe(
                tap(() => this.changed.emit()),
                catchError(() => {
                    this.resetFuelPrices();
                    return EMPTY;
                }),
            )
            .subscribe();
    }

    protected onRowEditCancel(): void {
        this.resetFuelPrices();
    }

    private resetFuelPrices(): void {
        this.fuelPrices.set(this.fuelStation().clone().fuelPrices);
    }
}
