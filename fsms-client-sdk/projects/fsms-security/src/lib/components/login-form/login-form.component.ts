import { Component, EventEmitter, Input, Output } from '@angular/core';
import { MessageModule } from 'primeng/message';
import { FormGroup, FormControl, Validators, ReactiveFormsModule } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { CommonModule } from '@angular/common';
import { InputGroupModule } from 'primeng/inputgroup';
import { InputGroupAddonModule } from 'primeng/inputgroupaddon';
import { InputTextModule } from 'primeng/inputtext';

@Component({
  selector: 'fsms-security-login-form',
  standalone: true,
  imports: [
    CommonModule, 
    MessageModule, 
    ReactiveFormsModule, 
    ButtonModule, 
    InputGroupModule, 
    InputGroupAddonModule, 
    InputTextModule
  ],
  templateUrl: './login-form.component.html'
})
export class LoginFormComponent {

  @Input() loading: boolean = false;
  @Output() formSubmitted = new EventEmitter<{ email: string, password: string }>();

  loginForm: FormGroup = new FormGroup({
    email: new FormControl('', [Validators.required, Validators.minLength(12), Validators.maxLength(30), Validators.email]), 
    password: new FormControl('', [Validators.required, Validators.minLength(4), Validators.maxLength(32)])
  })

  handleSubmit(): void {
    if(this.loginForm.valid) {
      this.formSubmitted.emit(this.loginForm.value);
      return;
    } 

    this.loginForm.markAllAsTouched();
  }

  get emailInvalid(): boolean {
    return this.isFieldInvalid(this.loginForm, "email");
  }

  get passwordInvalid(): boolean {
    return this.isFieldInvalid(this.loginForm, "password");
  }

  // TODO rewrite it to util
  private isFieldInvalid(formGroup: FormGroup, fieldName: string): boolean {
    return !!(formGroup.get(fieldName)?.touched && formGroup.get(fieldName)?.invalid);
  }

}
