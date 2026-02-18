import { Component, inject, Signal } from '@angular/core';
import { DialogModule } from 'primeng/dialog';
import { CommonModule } from '@angular/common';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { MessageModule } from 'primeng/message';
import { SelectModule } from 'primeng/select';
import { InputNumberModule } from 'primeng/inputnumber';
import { toSignal } from '@angular/core/rxjs-interop';
import BasicDialog from '../../../common/basic-dialog.component';
import { FuelGrade } from 'fsms-web-api';
import { CreateFuelOrderHandler } from '../../../fuel-station/handlers/create-fuel-order.handler';
import { FuelStationStore } from '../../../fuel-station/fuel-station-store';
import { tap } from 'rxjs';


@Component({
  selector: 'app-create-fuel-order-dialog',
  imports: [CommonModule, ReactiveFormsModule, DialogModule, ButtonModule, MessageModule, InputTextModule, SelectModule, InputNumberModule],
  templateUrl: './create-fuel-order-dialog.component.html'
})
export class CreateFuelOrderDialogComponent extends BasicDialog {
  private readonly handler = inject(CreateFuelOrderHandler);
  private readonly store = inject(FuelStationStore);

  protected readonly loading = toSignal(this.handler.loading$, { initialValue: false });

  protected readonly fuelGrades = [
    { label: "Diesel", value: FuelGrade.Diesel },
    { label: "RON 95", value: FuelGrade.RON_95 },
    { label: "RON 92", value: FuelGrade.RON_92 }
  ];

  protected readonly createFuelOrderForm = new FormGroup({
    fuelGrade: new FormControl<FuelGrade | null>(null, Validators.required),
    amount: new FormControl<number>(0, Validators.required)
  });

  handleFormSubmission() {
    if (this.createFuelOrderForm.valid) {
        console.log(this.createFuelOrderForm.value);
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
}
