import { Component, inject } from '@angular/core';
import { DialogModule } from 'primeng/dialog';
import { ManagerFormComponent, ManagerFormData } from '../manager-form/manager-form.component';
import BasicDialog from '../../../common/basic-dialog.component';
import { CreateManagerHandler } from '../../commands/create-manager-handler';
import { toSignal } from '@angular/core/rxjs-interop';

@Component({
  selector: 'app-create-manager-dialog',
  imports: [DialogModule, ManagerFormComponent],
  templateUrl: './create-manager-dialog.component.html'
})
export class CreateManagerDialogComponent extends BasicDialog {

  private readonly createManagerHandler = inject(CreateManagerHandler);

  loading = toSignal(this.createManagerHandler.loading$, {initialValue: false});

  handleFormSubmission({email, firstName, lastName}: ManagerFormData) {
    this.createManagerHandler.handle({
      email, 
      firstName, 
      lastName
    });
  }

}
