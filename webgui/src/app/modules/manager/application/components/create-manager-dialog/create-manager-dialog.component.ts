import { Component, inject, Input } from '@angular/core';
import { DialogModule } from 'primeng/dialog';
import { ManagerFormComponent } from '../manager-form/manager-form.component';
import ManagersQueryService from '../../managers-query.service';
import { MessageService } from 'primeng/api';
import { ManagerApiService } from '../../../infrastructure/manager-api.service';
import { ManagerFormData } from '../../manager.model';
import { finalize } from 'rxjs';
import BasicDialog from '../../../../common/application/basic-dialog.component';

@Component({
  selector: 'app-create-manager-dialog',
  imports: [DialogModule, ManagerFormComponent],
  templateUrl: './create-manager-dialog.component.html'
})
export class CreateManagerDialogComponent extends BasicDialog {

  private managersQueryService: ManagersQueryService = inject(ManagersQueryService);
  private managerApiService: ManagerApiService = inject(ManagerApiService);
  private messageService: MessageService = inject(MessageService);

  loading = false;

  handleFormSubmission(e: ManagerFormData) {
    this.loading = true;
    this.managerApiService.createManager(e.firstName, e.lastName, e.email)
      .pipe(finalize(() => this.loading = false))
      .subscribe({
        next: (d) => {
          // TODO This logic should be handled inside service 
          this.managersQueryService.getManagers()
            .subscribe({
              error: () => this.messageService.add({ severity: "error", summary: "Error", detail: "An error while fetching manager"})
            });
          this.messageService.add({ severity: "success", summary: "Created", detail: "A new manager was created"});
          this.closeDialog();
        },
        error: (err) => {
          console.error("Error: ", err);
          this.messageService.add({severity: "error", summary: "Error", detail: "An error occurred while creating manager"});
        }
      })
  }

}
