import { Component, inject } from '@angular/core';
import { DialogModule } from 'primeng/dialog';
import { CommonModule } from '@angular/common';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { MessageModule } from 'primeng/message';
import { MessageService } from 'primeng/api';
import { Observable } from 'rxjs';
import { SelectModule } from 'primeng/select';
import { InputNumberModule } from 'primeng/inputnumber';
import BasicDialog from '../../../common/basic-dialog.component';
import ManagerFuelStationContextService from '../../../fuel-station/services/manager-fuel-station-context.service';
import { FuelGrade } from 'fsms-web-api';

@Component({
  selector: 'app-create-fuel-order-dialog',
  imports: [CommonModule ,ReactiveFormsModule, DialogModule, ButtonModule, MessageModule, InputTextModule, SelectModule, InputNumberModule],
  templateUrl: './create-fuel-order-dialog.component.html'
})
export class CreateFuelOrderDialogComponent extends BasicDialog {
  private fuelStationContext: ManagerFuelStationContextService = inject(ManagerFuelStationContextService);
  private messageService: MessageService = inject(MessageService);

  fuelGrades = [
    { label: "Diesel", value: FuelGrade.Diesel },
    { label: "RON 95", value: FuelGrade.RON_95 },
    { label: "RON 92", value: FuelGrade.RON_92 }
  ]
  createFuelOrderForm = new FormGroup({
    fuelGrade: new FormControl<{ label: string, value: FuelGrade } | null>(null, Validators.required),
    amount: new FormControl<number>(0, Validators.required)
  })

  handleFormSubmission() {
    if(this.createFuelOrderForm.valid) {
      console.log(this.createFuelOrderForm.value)
      // TODO make form data interface
      const formData = {
        fuelGrade: this.createFuelOrderForm.value.fuelGrade,
        amount: this.createFuelOrderForm.value.amount
      } as unknown as { fuelGrade: FuelGrade, amount: number} ;
      this.fuelStationContext.createFuelOrder(formData.fuelGrade, formData.amount)
        .subscribe({
          next: () => {
            this.messageService.add({ severity: "success", summary: "Created", detail: "Fuel order was successfully assigned" });
            this.closeDialog();
          },
          error: () => this.messageService.add({ severity: "error", summary: "Error", detail: "An error occurred while creating fuel order"})
        });
    }

    this.createFuelOrderForm.markAllAsTouched();
  }

  get fuelGradeInvalid() {
    return this.isFieldInvalid(this.createFuelOrderForm, "fuelGrade");
  }

  get amountInvalid() {
    return this.isFieldInvalid(this.createFuelOrderForm, "amount");
  }

  get loading$(): Observable<boolean> {
    return this.fuelStationContext.loading.createFuelOrder;
  }

  // TODO rewrite it to util
  private isFieldInvalid(formGroup: FormGroup, fieldName: string): boolean {
    return !!(formGroup.get(fieldName)?.touched && formGroup.get(fieldName)?.invalid);
  }
}
