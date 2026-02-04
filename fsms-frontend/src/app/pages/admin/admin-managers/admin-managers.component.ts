import { Component, inject, OnInit } from '@angular/core';
import ManagersQueryService from '../../../modules/manager/services/managers-query.service';
import { MessageService } from 'primeng/api';
import { CommonModule } from '@angular/common';
import { ButtonModule } from 'primeng/button';
import { TableModule } from 'primeng/table';
import { SkeletonModule } from 'primeng/skeleton';
import { PanelModule } from 'primeng/panel';
import { TagModule } from 'primeng/tag';
import { CreateManagerDialogComponent } from '../../../modules/manager/components/create-manager-dialog/create-manager-dialog.component';
import { Manager, ManagerStatus } from "fsms-web-api";

@Component({
  selector: 'app-admin-managers',
  imports: [CommonModule, ButtonModule, TableModule, SkeletonModule, PanelModule, TagModule, CreateManagerDialogComponent],
  templateUrl: './admin-managers.component.html'
})
export class AdminManagersComponent implements OnInit {
  
  private managersQueryService: ManagersQueryService = inject(ManagersQueryService);
  private messageService: MessageService = inject(MessageService);

  managers: Manager[] = [];
  skeletonRows = new Array(5).fill(null);
  skeletonCols = new Array(6).fill(null);
  
  ngOnInit(): void {
    this.managersQueryService.getManagers()
      .subscribe({
        error: () => this.messageService.add({ severity: "error", summary: "Error", detail: "An error occurred while fetching managers" })
      })
    this.managersQueryService.managers$.subscribe((data) => this.managers = data)
  }

  getSeverity(manager: Manager): "success" | undefined {
    if(manager.status === ManagerStatus.Active) {
      return "success";
    }
    return undefined;
  }

  get loading$() {
    return this.managersQueryService.loading$;
  }

}
