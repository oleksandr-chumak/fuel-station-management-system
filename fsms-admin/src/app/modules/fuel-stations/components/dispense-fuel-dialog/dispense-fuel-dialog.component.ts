import { CommonModule } from '@angular/common';
import { Component, inject, Input } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { DialogModule } from 'primeng/dialog';
import { InputNumberModule } from 'primeng/inputnumber';
import { tap } from 'rxjs';
import { toSignal } from '@angular/core/rxjs-interop';
import BasicDialog from '../../../common/basic-dialog.component';
import { DispenseFuelHandler } from '../../handlers/dispense-fuel-handler';
import { FuelGrade } from 'fsms-web-api';

@Component({
  selector: 'app-dispense-fuel-dialog',
  imports: [CommonModule, FormsModule, DialogModule, ButtonModule, InputNumberModule],
  templateUrl: './dispense-fuel-dialog.component.html'
})
export class DispenseFuelDialogComponent extends BasicDialog {

  @Input() fuelStationId!: number;
  @Input() fuelTankId!: number;
  @Input() fuelGrade!: FuelGrade;
  @Input() availableVolume = 0;

  private readonly handler = inject(DispenseFuelHandler);
  readonly loading = toSignal(this.handler.loading$, { initialValue: false });

  volume: number | null = null;

  fuelGradeLabel(): string {
    return FuelGrade[this.fuelGrade];
  }

  override openDialog(): void {
    this.volume = null;
    super.openDialog();
  }

  get canSubmit(): boolean {
    return this.volume !== null
        && this.volume > 0
        && this.volume <= this.availableVolume;
  }

  submit(): void {
    if (!this.canSubmit) {
      return;
    }
    this.handler.handle({
      fuelStationId: this.fuelStationId,
      fuelTankId: this.fuelTankId,
      volume: this.volume!,
    })
      .pipe(tap(() => this.closeDialog()))
      .subscribe();
  }
}
