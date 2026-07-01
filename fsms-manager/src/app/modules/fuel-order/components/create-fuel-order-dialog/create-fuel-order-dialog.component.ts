import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DialogModule } from 'primeng/dialog';
import { toSignal } from '@angular/core/rxjs-interop';
import { TranslatePipe } from '@ngx-translate/core';
import { tap } from 'rxjs';
import BasicDialog from '../../../common/basic-dialog.component';
import { CreateFuelOrderHandler } from '../../../fuel-station/handlers/create-fuel-order.handler';
import { FuelStationStore } from '../../../fuel-station/fuel-station-store';
import { CreateFuelOrderForm, CreateFuelOrderFormValue } from './create-fuel-order-form';

@Component({
  selector: 'app-create-fuel-order-dialog',
  imports: [CommonModule, DialogModule, TranslatePipe, CreateFuelOrderForm],
  templateUrl: './create-fuel-order-dialog.component.html'
})
export class CreateFuelOrderDialogComponent extends BasicDialog {
  private readonly handler = inject(CreateFuelOrderHandler);
  private readonly store = inject(FuelStationStore);

  protected readonly loading = toSignal(this.handler.loading$, { initialValue: false });
  protected readonly fuelStation = toSignal(this.store.fuelStation$, { initialValue: null });

  protected handleSubmit(payload: CreateFuelOrderFormValue): void {
    this.handler
      .handle({
        fuelStationId: this.store.fuelStation.fuelStationId,
        fuelGrade: payload.fuelGrade,
        allocations: payload.allocations,
      })
      .pipe(tap(() => this.closeDialog()))
      .subscribe();
  }
}
