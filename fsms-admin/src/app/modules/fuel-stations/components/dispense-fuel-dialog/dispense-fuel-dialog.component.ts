import { CommonModule } from '@angular/common';
import { Component, inject, Input, OnChanges, SimpleChanges } from '@angular/core';
import { FormControl, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { DialogModule } from 'primeng/dialog';
import { InputNumberModule } from 'primeng/inputnumber';
import { tap } from 'rxjs';
import { toSignal } from '@angular/core/rxjs-interop';
import BasicDialog from '../../../common/basic-dialog.component';
import { DispenseFuelHandler } from '../../handlers/dispense-fuel-handler';
import { FuelGrade } from 'fsms-web-api';
import { TranslatePipe } from '@ngx-translate/core';
import { FuelGradeLabel } from '../../../fuel-prices/components/fuel-grade-label/fuel-grade-label';

@Component({
  selector: 'app-dispense-fuel-dialog',
  imports: [CommonModule, FormsModule, ReactiveFormsModule, DialogModule, ButtonModule, InputNumberModule, TranslatePipe, FuelGradeLabel],
  templateUrl: './dispense-fuel-dialog.component.html'
})
export class DispenseFuelDialogComponent extends BasicDialog implements OnChanges {

  @Input() fuelStationId!: number;
  @Input() fuelTankId!: number;
  @Input() fuelGrade!: FuelGrade;
  @Input() availableVolume = 0;

  private readonly handler = inject(DispenseFuelHandler);
  readonly loading = toSignal(this.handler.loading$, { initialValue: false });

  readonly volumeControl = new FormControl<number | null>(null, [
    Validators.required,
    Validators.min(0.001),
  ]);

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['availableVolume']) {
      this.volumeControl.setValidators([
        Validators.required,
        Validators.min(0.001),
        Validators.max(this.availableVolume),
      ]);
      this.volumeControl.updateValueAndValidity({ emitEvent: false });
    }
  }

  override openDialog(): void {
    this.volumeControl.reset(null);
    super.openDialog();
  }

  get volumeInvalid(): boolean {
    return this.volumeControl.touched && this.volumeControl.invalid;
  }

  get exceedsAvailable(): boolean {
    return this.volumeControl.hasError('max');
  }

  get belowMinimum(): boolean {
    return this.volumeControl.hasError('min') || this.volumeControl.hasError('required');
  }

  submit(): void {
    if (this.volumeControl.invalid) {
      this.volumeControl.markAsTouched();
      return;
    }
    this.handler.handle({
      fuelStationId: this.fuelStationId,
      fuelTankId: this.fuelTankId,
      volume: this.volumeControl.value!,
    })
      .pipe(tap(() => this.closeDialog()))
      .subscribe();
  }
}
