import { Component, inject, Input } from '@angular/core';
import { DialogModule } from 'primeng/dialog';
import { ManagerSelectComponent } from '../manager-select/manager-select.component';
import { ButtonModule } from 'primeng/button';
import { MessageModule } from 'primeng/message';
import { InputTextModule } from 'primeng/inputtext';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { tap } from 'rxjs';
import { CommonModule } from '@angular/common';
import BasicDialog from '../../../common/basic-dialog.component';
import { Manager } from 'fsms-web-api';
import { AssignManagerHandler } from '../../../fuel-stations/handlers/assign-manager-handler';
import { FuelStationStore } from '../../../fuel-stations/fuel-station-store';
import { toSignal } from '@angular/core/rxjs-interop';

@Component({
  selector: 'app-assign-manager-dialog',
  imports: [CommonModule, ReactiveFormsModule, DialogModule, ManagerSelectComponent, ButtonModule, MessageModule, InputTextModule],
  templateUrl: './assign-manager-dialog.component.html'
})
export class AssignManagerDialogComponent extends BasicDialog {

  private readonly fuelStationStore = inject(FuelStationStore);
  private readonly assignManagerHandler = inject(AssignManagerHandler);

  readonly loading = toSignal(this.assignManagerHandler.loading$, { initialValue: false });
  readonly assignManagerForm = new FormGroup({
    manager: new FormControl<Manager | null>(null, Validators.required)
  })

  handleFormSubmission() {
    if (this.assignManagerForm.valid) {
      const formData = this.assignManagerForm.value as { manager: Manager };
      this.assignManagerHandler.handle({
        fuelStationId: this.fuelStationStore.fuelStation.fuelStationId,
        managerId: formData.manager.credentialsId
      })
      .pipe(tap(() => this.closeDialog()))
      .subscribe()
    }

    this.assignManagerForm.markAllAsTouched();
  }

  handleMangerAssigned(manager: Manager) {
    this.assignManagerForm.patchValue({ manager });
  }

  get managerInvalid() {
    return this.isFieldInvalid(this.assignManagerForm, "manager");
  }

  // TODO rewrite it to util
  private isFieldInvalid(formGroup: FormGroup, fieldName: string): boolean {
    return !!(formGroup.get(fieldName)?.touched && formGroup.get(fieldName)?.invalid);
  }
}
