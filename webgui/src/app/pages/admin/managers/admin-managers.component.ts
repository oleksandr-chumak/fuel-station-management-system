import { Component, inject, OnInit } from '@angular/core';
import { MessageService } from 'primeng/api';
import { CommonModule } from '@angular/common';
import { ButtonModule } from 'primeng/button';
import { TableModule } from 'primeng/table';
import { SkeletonModule } from 'primeng/skeleton';
import { PanelModule } from 'primeng/panel';
import Manager from '../../../modules/manager/models/manager.model';
import { TagModule } from 'primeng/tag';
import ManagerStatus from '../../../modules/manager/models/manager-status.enum';
import { CreateManagerDialogComponent } from '../../../modules/manager/components/create-manager-dialog/create-manager-dialog.component';
import ManagerService from '../../../modules/manager/services/manager.service';

@Component({
  selector: 'app-admin-managers',
  imports: [CommonModule, ButtonModule, TableModule, SkeletonModule, PanelModule, TagModule, CreateManagerDialogComponent],
  templateUrl: './admin-managers.component.html'
})
export class AdminManagersComponent implements OnInit {
  private managerService: ManagerService = inject(ManagerService);
  private messageService: MessageService = inject(MessageService);

  managers: Manager[] = [];
  skeletonRows = new Array(5).fill(null);
  skeletonCols = new Array(6).fill(null);
  
  ngOnInit(): void {
    this.getManagers();
  }

  getManagers() {
    this.managerService.getManagers()
      .subscribe({
        error: () => this.messageService.add({ severity: 'error', summary: 'Error', detail: 'An error occurred while fetching managers' }),
        next: (managers) => this.managers = managers 
      })
  }

  getSeverity(manager: Manager): 'success' | undefined {
    if(manager.status === ManagerStatus.Active) {
      return 'success';
    }
    return undefined;
  }

  get loading$() {
    return this.managerService.loading$;
  }
}
