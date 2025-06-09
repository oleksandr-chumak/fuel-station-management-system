import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, Output } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { MessageModule } from 'primeng/message';

import { ManagerFormData } from '../../interfaces/manager-form-data.interface';

@Component({
  selector: 'app-manager-form',
  imports: [
    CommonModule, 
    MessageModule, 
    ReactiveFormsModule, 
    ButtonModule, 
    InputTextModule
  ],
  templateUrl: './manager-form.component.html'
})
export class ManagerFormComponent {
  @Input() loading = false;
  @Input() buttonText = '';
  @Output() formSubmitted = new EventEmitter<ManagerFormData>();

  managerForm = new FormGroup({
    firstName: new FormControl('', Validators.required),
    lastName: new FormControl('', Validators.required),
    email: new FormControl('', [Validators.required, Validators.email]),
  });
  
  handleSubmit() {
    if(this.managerForm.valid) {
      this.formSubmitted.emit(this.managerForm.value as ManagerFormData);
    }
    this.managerForm.markAllAsTouched();
  }

  get firstNameInvalid() {
    return this.isFieldInvalid(this.managerForm, 'firstName');
  }

  get lastNameInvalid() {
    return this.isFieldInvalid(this.managerForm, 'lastName');
  }

  get emailInvalid() {
    return this.isFieldInvalid(this.managerForm, 'email');
  }

  private isFieldInvalid(formGroup: FormGroup, fieldName: string): boolean {
    return !!(formGroup.get(fieldName)?.touched && formGroup.get(fieldName)?.invalid);
  }
}
