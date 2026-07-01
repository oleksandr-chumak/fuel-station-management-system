import { Component, EventEmitter, Input, Output } from '@angular/core';
import { InputTextModule } from 'primeng/inputtext';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { ButtonModule } from 'primeng/button';
import { MessageModule } from 'primeng/message';
import { Select } from 'primeng/select';
import { CountryCode } from 'fsms-web-api';
import { TranslatePipe } from '@ngx-translate/core';

export interface FuelStationFormData {
    street: string,
    buildingNumber: string,
    city: string,
    postalCode: string,
    country: CountryCode,
    address: string,
}

@Component({
  selector: 'app-fuel-station-form',
  standalone: true,
  imports: [
    CommonModule,
    MessageModule,
    ReactiveFormsModule,
    ButtonModule,
    InputTextModule,
    Select,
    TranslatePipe,
  ],
  templateUrl: './fuel-station-form.component.html'
})
export class FuelStationFormComponent {
  @Input() loading: boolean = false;
  @Input() buttonText: string = "";
  @Input() disabled: boolean = false;
  @Output() formSubmitted = new EventEmitter<FuelStationFormData>();

  protected readonly countryCodes = Object.values(CountryCode);

  private static readonly STREET_PATTERN = /^[\p{L}0-9\s\-.,']+$/u;
  private static readonly BUILDING_NUMBER_PATTERN = /^[\p{L}0-9\-/]+$/u;
  private static readonly CITY_PATTERN = /^[\p{L}\s\-']+$/u;
  private static readonly POSTAL_CODE_PATTERN =
    /^(?:[0-9]{4,6}|[A-Za-z][0-9][A-Za-z]\s?[0-9][A-Za-z][0-9]|[0-9]{5}-[0-9]{4})$/;

  fuelStationForm = new FormGroup({
     street: new FormControl('', [
       Validators.required,
       Validators.minLength(2),
       Validators.maxLength(100),
       Validators.pattern(FuelStationFormComponent.STREET_PATTERN),
     ]),
     buildingNumber: new FormControl('', [
       Validators.required,
       Validators.minLength(1),
       Validators.maxLength(20),
       Validators.pattern(FuelStationFormComponent.BUILDING_NUMBER_PATTERN),
     ]),
     city: new FormControl('', [
       Validators.required,
       Validators.minLength(2),
       Validators.maxLength(50),
       Validators.pattern(FuelStationFormComponent.CITY_PATTERN),
     ]),
     postalCode: new FormControl('', [
       Validators.required,
       Validators.pattern(FuelStationFormComponent.POSTAL_CODE_PATTERN),
     ]),
     country: new FormControl<CountryCode | null>(null, Validators.required),
  })
  
  handleSubmit() {
    if(this.fuelStationForm.valid) {
      this.formSubmitted.emit(this.fuelStationForm.value as FuelStationFormData);
    }
    this.fuelStationForm.markAllAsTouched();
  }

  get streetInvalid() {
    return this.isFieldInvalid(this.fuelStationForm, "street");
  }

  get buildingNumberInvalid() {
    return this.isFieldInvalid(this.fuelStationForm, "buildingNumber");
  }

  get cityInvalid() {
    return this.isFieldInvalid(this.fuelStationForm, "city");
  }

  get postalCodeInvalid() {
    return this.isFieldInvalid(this.fuelStationForm, "postalCode");
  }

  get countryInvalid() {
    return this.isFieldInvalid(this.fuelStationForm, "country");
  }

  // TODO rewrite it to util
  private isFieldInvalid(formGroup: FormGroup, fieldName: string): boolean {
    return !!(formGroup.get(fieldName)?.touched && formGroup.get(fieldName)?.invalid);
  }

}
