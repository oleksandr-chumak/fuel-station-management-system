import { Component, inject, Input } from '@angular/core';
import { DialogModule } from 'primeng/dialog';
import { ManagerSelectComponent } from '../manager-select/manager-select.component';
import { ButtonModule } from 'primeng/button';
import { MessageModule } from 'primeng/message';
import { InputTextModule } from 'primeng/inputtext';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import Manager from '../../../domain/manager.model';
import AdminFuelStationContextService from '../../../../fuel-order/domain/admin-fuel-station-context.service';
import { MessageService } from 'primeng/api';
import { Observable } from 'rxjs';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-assign-manager-dialog',
  imports: [CommonModule ,ReactiveFormsModule, DialogModule, ManagerSelectComponent, ButtonModule, MessageModule, InputTextModule],
  templateUrl: './assign-manager-dialog.component.html'
})
export class AssignManagerDialogComponent {

  @Input() visible: boolean = false;

  private fuelStationContext: AdminFuelStationContextService = inject(AdminFuelStationContextService);
  private messageService: MessageService = inject(MessageService);

  assignManagerForm = new FormGroup({
    manager: new FormControl<Manager | null>(null, Validators.required)
  })

  handleFormSubmission() {
    if(this.assignManagerForm.valid) {
      // TODO make form data interface
      const formData = this.assignManagerForm.value as { manager: Manager};
      this.fuelStationContext.assignManager(formData.manager.id)
        .subscribe({
          next: () => this.visible = false,
          error: () => this.messageService.add({ severity: "error", summary: "Error", detail: "An error occurred while assigning manager"})
        });
    }

    this.assignManagerForm.markAllAsTouched();
  }

  handleMangerAssigned(manager: Manager) {
    this.assignManagerForm.patchValue({ manager });
  }

  get managerInvalid() {
    return this.isFieldInvalid(this.assignManagerForm, "manager");
  }


  get loading$(): Observable<boolean> {
    return this.fuelStationContext.loading.assignManager;
  }

  // TODO rewrite it to util
  private isFieldInvalid(formGroup: FormGroup, fieldName: string): boolean {
    return !!(formGroup.get(fieldName)?.touched && formGroup.get(fieldName)?.invalid);
  }
}
