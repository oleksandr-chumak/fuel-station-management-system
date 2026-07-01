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
import { FuelStation, FuelStationFuelPrice } from 'fsms-web-api';
import { MoneyPipe } from '../../../../../../modules/common/money.pipe';
import { ChangeFuelPriceHandler } from '../../../../../../modules/fuel-stations/handlers/change-fuel-price-handler';
import { TranslatePipe, TranslateService } from '@ngx-translate/core';
import { FuelGradeLabel } from '../../../../../../modules/fuel-prices/components/fuel-grade-label/fuel-grade-label';

@Component({
    selector: 'app-fuel-price-table',
    imports: [CommonModule, FormsModule, InputTextModule, PanelModule, SkeletonModule, TableModule, ButtonModule, MoneyPipe, TranslatePipe, FuelGradeLabel],
    templateUrl: './fuel-price-table.html',
})
export class FuelPriceTable {
    fuelStation = input.required<FuelStation>();
    changed = output<void>();

    private readonly changeFuelPriceHandler = inject(ChangeFuelPriceHandler);
    private readonly messageService = inject(MessageService);
    private readonly translate = inject(TranslateService);

    protected readonly fuelPrices = signal<FuelStationFuelPrice[]>([]);
    protected readonly loading = toSignal(this.changeFuelPriceHandler.loading$, { initialValue: false });
    protected readonly editingInvalid = signal<Record<string, boolean>>({});

    protected readonly skeletonRows = new Array(5).fill(null);
    protected readonly skeletonCols = new Array(3).fill(null);

    constructor() {
        effect(() => {
            this.fuelPrices.set(this.fuelStation().fuelPrices);
        });
    }

    protected onRowEditInit(): void {
        this.resetFuelPrices();
        this.editingInvalid.set({});
    }

    protected onPriceInput(fuelPrice: FuelStationFuelPrice): void {
        const parsed = Number(fuelPrice.pricePerLiter);
        const invalid = fuelPrice.pricePerLiter === null
            || fuelPrice.pricePerLiter === undefined
            || (fuelPrice.pricePerLiter as unknown as string) === ''
            || Number.isNaN(parsed)
            || parsed <= 0;
        this.editingInvalid.update((state) => ({ ...state, [fuelPrice.fuelGrade]: invalid }));
    }

    protected isEditingInvalid(fuelPrice: FuelStationFuelPrice): boolean {
        return !!this.editingInvalid()[fuelPrice.fuelGrade];
    }

    protected onRowEditSave(fuelPrice: FuelStationFuelPrice): void {
        const newFuelPrice = Number(fuelPrice.pricePerLiter);

        if (Number.isNaN(newFuelPrice) || newFuelPrice <= 0) {
            this.resetFuelPrices();
            this.editingInvalid.set({});
            this.messageService.add({
                severity: 'error',
                summary: this.translate.instant('fuelPrices.validation'),
                detail: this.translate.instant('fuelPrices.validationFuelPriceNumber'),
            });
            return;
        }

        this.editingInvalid.update((state) => ({ ...state, [fuelPrice.fuelGrade]: false }));

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
        this.editingInvalid.set({});
    }

    private resetFuelPrices(): void {
        this.fuelPrices.set(this.fuelStation().clone().fuelPrices);
    }
}
