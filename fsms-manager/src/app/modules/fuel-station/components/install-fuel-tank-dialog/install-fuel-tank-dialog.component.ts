import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { DialogModule } from 'primeng/dialog';
import { InputNumberModule } from 'primeng/inputnumber';
import { MessageModule } from 'primeng/message';
import { SelectModule } from 'primeng/select';
import { tap } from 'rxjs';
import { toSignal } from '@angular/core/rxjs-interop';
import BasicDialog from '../../../common/basic-dialog.component';
import { InstallFuelTankHandler } from '../../handlers/install-fuel-tank.handler';
import { FuelStationStore } from '../../fuel-station-store';
import { FuelGrade } from 'fsms-web-api';

@Component({
  selector: 'app-install-fuel-tank-dialog',
  imports: [CommonModule, ReactiveFormsModule, DialogModule, ButtonModule, MessageModule, SelectModule, InputNumberModule],
  templateUrl: './install-fuel-tank-dialog.component.html'
})
export class InstallFuelTankDialogComponent extends BasicDialog {

  private readonly handler = inject(InstallFuelTankHandler);
  private readonly store = inject(FuelStationStore);

  protected readonly loading = toSignal(this.handler.loading$, { initialValue: false });

  protected readonly fuelGrades = [
    { label: 'Diesel', value: FuelGrade.Diesel },
    { label: 'RON 95', value: FuelGrade.RON_95 },
    { label: 'RON 92', value: FuelGrade.RON_92 }
  ];

  protected readonly form = new FormGroup({
    fuelGrade: new FormControl<FuelGrade | null>(null, Validators.required),
    maxCapacity: new FormControl<number | null>(null, [Validators.required, Validators.min(0.001)])
  });

  override openDialog(): void {
    this.form.reset();
    super.openDialog();
  }

  submit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    this.handler
      .handle({
        fuelStationId: this.store.fuelStation.fuelStationId,
        fuelGrade: this.form.value.fuelGrade!,
        maxCapacity: this.form.value.maxCapacity!,
      })
      .pipe(tap(() => this.closeDialog()))
      .subscribe();
  }

  get fuelGradeInvalid(): boolean {
    const c = this.form.get('fuelGrade');
    return !!(c?.touched && c?.invalid);
  }

  get maxCapacityInvalid(): boolean {
    const c = this.form.get('maxCapacity');
    return !!(c?.touched && c?.invalid);
  }
}
