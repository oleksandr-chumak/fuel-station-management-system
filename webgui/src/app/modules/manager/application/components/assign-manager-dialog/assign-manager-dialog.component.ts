import { CommonModule } from '@angular/common';
import { Component, inject, OnInit } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MessageService } from 'primeng/api';
import { ButtonModule } from 'primeng/button';
import { DialogModule } from 'primeng/dialog';
import { InputTextModule } from 'primeng/inputtext';
import { MessageModule } from 'primeng/message';

import { BasicDialog } from '~common/application/basic-dialog.component';
import { AdminFuelStationContextLoadingEvent } from '~fuel-station/application/models/admin-fuel-station-context-loading-event.enum';
import { AdminFuelStationContextService } from '~fuel-station/application/services/admin-fuel-station-context.service';
import { Manager } from '~manager/api/models/manager.model';
import { ManagerSelectComponent } from '~manager/application/components/manager-select/manager-select.component';

@Component({
  selector: 'app-assign-manager-dialog',
  imports: [CommonModule, ReactiveFormsModule, DialogModule, ManagerSelectComponent, ButtonModule, MessageModule, InputTextModule],
  templateUrl: './assign-manager-dialog.component.html'
})
export class AssignManagerDialogComponent extends BasicDialog implements OnInit {
  private fuelStationContext: AdminFuelStationContextService = inject(AdminFuelStationContextService);
  private messageService: MessageService = inject(MessageService);

  loading = false;
  assignManagerForm = new FormGroup({
    manager: new FormControl<Manager | null>(null, Validators.required)
  });

  ngOnInit(): void {
    this.fuelStationContext.loadingEvents$.subscribe((event) => {
      if(event?.type === AdminFuelStationContextLoadingEvent.ASSIGN_MANAGER) {
        this.loading = event.value;
      }
    });
  }

  handleFormSubmission() {
    if (this.assignManagerForm.valid) {
      // TODO make form data interface
      const formData = this.assignManagerForm.value as { manager: Manager };
      this.fuelStationContext.assignManager(formData.manager.id)
        .subscribe({
          next: () => {
            this.messageService.add({ severity: 'success', summary: 'Assigned', detail: 'Manager was successfully assigned' });
            this.closeDialog();
          },
          error: () => this.messageService.add({ severity: 'error', summary: 'Error', detail: 'An error occurred while assigning manager' })
        });
    }

    this.assignManagerForm.markAllAsTouched();
  }

  handleMangerAssigned(manager: Manager) {
    this.assignManagerForm.patchValue({ manager });
  }

  get managerInvalid() {
    return this.isFieldInvalid(this.assignManagerForm, 'manager');
  }

  // TODO rewrite it to util
  private isFieldInvalid(formGroup: FormGroup, fieldName: string): boolean {
    return !!(formGroup.get(fieldName)?.touched && formGroup.get(fieldName)?.invalid);
  }
}
