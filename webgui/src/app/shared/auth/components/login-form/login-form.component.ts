import { Component, EventEmitter, Input, Output } from '@angular/core';
import { MessageModule } from 'primeng/message';
import { FormGroup, FormControl, Validators, ReactiveFormsModule } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-login-form',
  imports: [CommonModule, MessageModule, ReactiveFormsModule, ButtonModule],
  templateUrl: './login-form.component.html'
})
export class LoginFormComponent {

  @Input() loading: boolean = false;
  @Output() formSubmitted = new EventEmitter<{ email: string, password: string }>();

  loginForm: FormGroup = new FormGroup({
    email: new FormControl('', [Validators.required, Validators.min(12), Validators.max(30), Validators.email]), 
    password: new FormControl('', [Validators.required, Validators.min(4), Validators.max(32)])
  })

  onSubmit(): void {
    if(this.loginForm.valid) {
      this.formSubmitted.emit(this.loginForm.value);
      return;
    } 

    this.loginForm.markAllAsTouched();
  }

  get emailValid(): boolean {
    return this.isFieldValid(this.loginForm, "password");
  }

  get passwordValid(): boolean {
    return this.isFieldValid(this.loginForm, "password");
  }

  // TODO rewrite it to util
  private isFieldValid(formGroup: FormGroup, fieldName: string): boolean {
    return !!(formGroup.get(fieldName)?.touched && formGroup.get(fieldName)?.valid);
  }

}
