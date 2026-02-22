import { Component, inject, output } from '@angular/core';
import { DialogModule } from 'primeng/dialog';
import { ManagerFormComponent, ManagerFormData } from '../manager-form/manager-form.component';
import BasicDialog from '../../../common/basic-dialog.component';
import { CreateManagerHandler } from '../../handlers/create-manager-handler';
import { toSignal } from '@angular/core/rxjs-interop';
import { tap } from 'rxjs';
import { Manager } from 'fsms-web-api';

@Component({
  selector: 'app-create-manager-dialog',
  imports: [DialogModule, ManagerFormComponent],
  templateUrl: './create-manager-dialog.component.html'
})
export class CreateManagerDialogComponent extends BasicDialog {
  managerCreated = output<Manager>();

  private readonly createManagerHandler = inject(CreateManagerHandler);

  protected readonly loading = toSignal(this.createManagerHandler.loading$, {initialValue: false});

  protected handleFormSubmission({email, firstName, lastName}: ManagerFormData) {
    this.createManagerHandler
      .handle({
        email, 
        firstName, 
        lastName
      })
      .pipe(tap((manager) => {
        this.managerCreated.emit(manager)
        this.closeDialog()
      }))
      .subscribe();
  }

}
