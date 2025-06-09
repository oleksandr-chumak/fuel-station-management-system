import { CommonModule } from '@angular/common';
import { Component, EventEmitter, inject, OnInit, Output } from '@angular/core';
import { MessageService } from 'primeng/api';
import { DialogModule } from 'primeng/dialog';

import { BasicDialog } from '../../../../common/application/basic-dialog.component';
import { ManagerFormData } from '../../interfaces/manager-form-data.interface';
import { Manager } from '../../../api/models/manager.model';
import { ManagerService } from '../../services/manager.service';
import { ManagerFormComponent } from '../manager-form/manager-form.component';

@Component({
  selector: 'app-create-manager-dialog',
  imports: [CommonModule, DialogModule, ManagerFormComponent],
  templateUrl: './create-manager-dialog.component.html'
})
export class CreateManagerDialogComponent extends BasicDialog implements OnInit {
  @Output() managerCreated = new EventEmitter<Manager>();

  private managerService: ManagerService = inject(ManagerService);
  private messageService: MessageService = inject(MessageService);
  loading = false;

  ngOnInit(): void {
    this.managerService.loading$.subscribe((v) => this.loading = v);
  }

  handleFormSubmission(e: ManagerFormData) {
    this.managerService.createManager(e.firstName, e.lastName, e.email)
      .subscribe({
        next: (data) => {
          this.messageService.add({ severity: 'success', summary: 'Created', detail: 'A new manager was created'});
          this.managerCreated.emit(data);
          this.closeDialog();
        },
        error: () => {
          this.messageService.add({severity: 'error', summary: 'Error', detail: 'An error occurred while creating manager'});
        }
      });
  }
}
