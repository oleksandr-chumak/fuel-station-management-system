import { Component, computed, inject } from '@angular/core';
import { DialogModule } from 'primeng/dialog';
import { CommonModule } from '@angular/common';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { MessageModule } from 'primeng/message';
import { SelectModule } from 'primeng/select';
import { InputNumberModule } from 'primeng/inputnumber';
import { toSignal } from '@angular/core/rxjs-interop';
import { TranslatePipe } from '@ngx-translate/core';
import BasicDialog from '../../../common/basic-dialog.component';
import { FuelGrade } from 'fsms-web-api';
import { CreateFuelOrderHandler } from '../../../fuel-station/handlers/create-fuel-order.handler';
import { FuelStationStore } from '../../../fuel-station/fuel-station-store';
import { tap } from 'rxjs';


@Component({
  selector: 'app-create-fuel-order-dialog',
  imports: [CommonModule, ReactiveFormsModule, DialogModule, ButtonModule, MessageModule, InputTextModule, SelectModule, InputNumberModule, TranslatePipe],
  templateUrl: './create-fuel-order-dialog.component.html'
})
export class CreateFuelOrderDialogComponent extends BasicDialog {
  private readonly handler = inject(CreateFuelOrderHandler);
  private readonly store = inject(FuelStationStore);

  protected readonly loading = toSignal(this.handler.loading$, { initialValue: false });

  private readonly fuelStation = toSignal(this.store.fuelStation$, { initialValue: null });

  protected readonly fuelGrades = computed(() => {
    const tanks = this.fuelStation()?.fuelTanks ?? [];
    const uniqueGrades = Array.from(new Set(tanks.map(t => t.fuelGrade)));
    return uniqueGrades.map(grade => ({
      labelKey: this.gradeLabelKey(grade),
      value: grade
    }));
  });

  protected readonly hasFuelGrades = computed(() => this.fuelGrades().length > 0);

  protected readonly createFuelOrderForm = new FormGroup({
    fuelGrade: new FormControl<FuelGrade | null>(null, Validators.required),
    amount: new FormControl<number>(0, Validators.required)
  });

  override openDialog(): void {
    this.createFuelOrderForm.reset({ fuelGrade: null, amount: 0 });
    super.openDialog();
  }

  handleFormSubmission() {
    if (this.createFuelOrderForm.valid) {
      this.handler
        .handle({
          fuelStationId: this.store.fuelStation.fuelStationId,
          fuelGrade: this.createFuelOrderForm.value.fuelGrade!,
          amount: this.createFuelOrderForm.value.amount!
        })
        .pipe(tap(() => this.closeDialog()))
        .subscribe();
    }

    this.createFuelOrderForm.markAllAsTouched();
  }

  get fuelGradeInvalid() {
    return this.isFieldInvalid(this.createFuelOrderForm, "fuelGrade");
  }

  get amountInvalid() {
    return this.isFieldInvalid(this.createFuelOrderForm, "amount");
  }

  private isFieldInvalid(formGroup: FormGroup, fieldName: string): boolean {
    return !!(formGroup.get(fieldName)?.touched && formGroup.get(fieldName)?.invalid);
  }

  private gradeLabelKey(grade: FuelGrade): string {
    const name = FuelGrade[grade];
    switch (name.toLowerCase().replace(/[-_\s]/g, '')) {
      case 'ron92': return 'fuelGrades.ron92';
      case 'ron95': return 'fuelGrades.ron95';
      case 'diesel': return 'fuelGrades.diesel';
      default: return 'fuelGrades.unknown';
    }
  }
}
