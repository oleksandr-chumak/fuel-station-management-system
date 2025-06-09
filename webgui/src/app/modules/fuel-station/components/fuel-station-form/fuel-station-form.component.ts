import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, Output } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { MessageModule } from 'primeng/message';

import { FuelStationFormData } from '../../interfaces/fuel-station-form-data.interface';

@Component({
  selector: 'app-fuel-station-form',
  standalone: true,
  imports: [
    CommonModule, 
    MessageModule, 
    ReactiveFormsModule, 
    ButtonModule, 
    InputTextModule
  ],
  templateUrl: './fuel-station-form.component.html'
})
export class FuelStationFormComponent {
  @Input() loading = false;
  @Input() buttonText = '';
  @Input() disabled = false;
  @Output() formSubmitted = new EventEmitter<FuelStationFormData>();

  fuelStationForm = new FormGroup({
    street: new FormControl('', Validators.required),
    buildingNumber: new FormControl('', Validators.required),
    city: new FormControl('', Validators.required),
    postalCode: new FormControl('', Validators.required),
    country: new FormControl('', Validators.required),
  })
  
  handleSubmit() {
    if(this.fuelStationForm.valid) {
      this.formSubmitted.emit(this.fuelStationForm.value as FuelStationFormData);
    }
    this.fuelStationForm.markAllAsTouched();
  }

  get streetInvalid() {
    return this.isFieldInvalid(this.fuelStationForm, 'street');
  }

  get buildingNumberInvalid() {
    return this.isFieldInvalid(this.fuelStationForm, 'buildingNumber');
  }

  get cityInvalid() {
    return this.isFieldInvalid(this.fuelStationForm, 'city');
  }

  get postalCodeInvalid() {
    return this.isFieldInvalid(this.fuelStationForm, 'postalCode');
  }

  get countryInvalid() {
    return this.isFieldInvalid(this.fuelStationForm, 'country');
  }

  // TODO rewrite it to util
  private isFieldInvalid(formGroup: FormGroup, fieldName: string): boolean {
    return !!(formGroup.get(fieldName)?.touched && formGroup.get(fieldName)?.invalid);
  }
}
