import { CommonModule } from '@angular/common';
import { Component, inject, OnInit } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MessageService } from 'primeng/api';
import { ButtonModule } from 'primeng/button';
import { DialogModule } from 'primeng/dialog';
import { InputNumberModule } from 'primeng/inputnumber';
import { InputTextModule } from 'primeng/inputtext';
import { MessageModule } from 'primeng/message';
import { SelectModule } from 'primeng/select';
import { Observable } from 'rxjs';

import { BasicDialog } from '../../../common/application/basic-dialog.component';
import { FuelGrade } from '../../../common/api/fuel-grade.enum';
import { ManagerFuelStationContextService } from '../../../fuel-station/application/services/manager-fuel-station-context.service';
import { ManagerFuelStationContextLoadingEvent } from '../../../fuel-station/application/models/manager-fuel-station-context-loading-event.enum';

@Component({
  selector: 'app-create-fuel-order-dialog',
  imports: [CommonModule ,ReactiveFormsModule, DialogModule, ButtonModule, MessageModule, InputTextModule, SelectModule, InputNumberModule],
  templateUrl: './create-fuel-order-dialog.component.html'
})
export class CreateFuelOrderDialogComponent extends BasicDialog implements OnInit {
  private fuelStationContext: ManagerFuelStationContextService = inject(ManagerFuelStationContextService);
  private messageService: MessageService = inject(MessageService);

  loading = false;
  fuelGrades = [
    { label: 'Diesel', value: FuelGrade.Diesel },
    { label: 'RON 95', value: FuelGrade.RON_95 },
    { label: 'RON 92', value: FuelGrade.RON_92 }
  ];

  createFuelOrderForm = new FormGroup({
    fuelGrade: new FormControl<{ label: string, value: FuelGrade } | null>(null, Validators.required),
    amount: new FormControl<number>(0, Validators.required)
  });

  ngOnInit(): void {
    this.fuelStationContext.loadingEvents$.subscribe((event) => {
      if(event?.type === ManagerFuelStationContextLoadingEvent.CREATE_FUEL_ORDER) {
        this.loading === event.value;
      }
    })
  }

  handleFormSubmission() {
    if(this.createFuelOrderForm.valid) {
      console.log(this.createFuelOrderForm.value);
      // TODO make form data interface
      const formData = {
        fuelGrade: this.createFuelOrderForm.value.fuelGrade,
        amount: this.createFuelOrderForm.value.amount
      } as unknown as { fuelGrade: FuelGrade, amount: number} ;
      this.fuelStationContext.createFuelOrder(formData.fuelGrade, formData.amount)
        .subscribe({
          next: () => {
            this.messageService.add({ severity: 'success', summary: 'Created', detail: 'Fuel order was successfully assigned' });
            this.closeDialog();
          },
          error: () => this.messageService.add({ severity: 'error', summary: 'Error', detail: 'An error occurred while creating fuel order'})
        });
    }

    this.createFuelOrderForm.markAllAsTouched();
  }

  get fuelGradeInvalid() {
    return this.isFieldInvalid(this.createFuelOrderForm, 'fuelGrade');
  }

  get amountInvalid() {
    return this.isFieldInvalid(this.createFuelOrderForm, 'amount');
  }

  // TODO rewrite it to util
  private isFieldInvalid(formGroup: FormGroup, fieldName: string): boolean {
    return !!(formGroup.get(fieldName)?.touched && formGroup.get(fieldName)?.invalid);
  }
}
