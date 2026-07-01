import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, Output } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { MessageModule } from 'primeng/message';
import { TranslatePipe } from '@ngx-translate/core';

export interface ManagerFormData {
    firstName: string;
    lastName: string;
    email: string;
}

@Component({
  selector: 'app-manager-form',
  imports: [
    CommonModule, 
    MessageModule, 
    ReactiveFormsModule, 
    ButtonModule,
    InputTextModule,
    TranslatePipe
  ],
  templateUrl: './manager-form.component.html'
})
export class ManagerFormComponent {

  @Input() loading: boolean = false;
  @Input() buttonText: string = "";
  @Output() formSubmitted = new EventEmitter<ManagerFormData>();

  private static readonly NAME_PATTERN = /^[\p{L}\s'-]+$/u;

  managerForm = new FormGroup({
     firstName: new FormControl('', [
       Validators.required,
       Validators.minLength(2),
       Validators.maxLength(50),
       Validators.pattern(ManagerFormComponent.NAME_PATTERN),
     ]),
     lastName: new FormControl('', [
       Validators.required,
       Validators.minLength(2),
       Validators.maxLength(50),
       Validators.pattern(ManagerFormComponent.NAME_PATTERN),
     ]),
     email: new FormControl('', [
       Validators.required,
       Validators.email,
       Validators.maxLength(100),
     ]),
  })
  
  handleSubmit() {
    if(this.managerForm.valid) {
      this.formSubmitted.emit(this.managerForm.value as ManagerFormData);
    }
    this.managerForm.markAllAsTouched();
  }

  get firstNameInvalid() {
    return this.isFieldInvalid(this.managerForm, "firstName");
  }

  get lastNameInvalid() {
    return this.isFieldInvalid(this.managerForm, "lastName");
  }

  get emailInvalid() {
    return this.isFieldInvalid(this.managerForm, "email");
  }

  private isFieldInvalid(formGroup: FormGroup, fieldName: string): boolean {
    return !!(formGroup.get(fieldName)?.touched && formGroup.get(fieldName)?.invalid);
  }

}
