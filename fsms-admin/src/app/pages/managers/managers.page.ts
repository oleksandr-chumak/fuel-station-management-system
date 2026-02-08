import { Component, inject, OnInit } from '@angular/core';
import ManagersQueryService from '../../modules/managers/services/managers-query.service';
import { MessageService } from 'primeng/api';
import { CommonModule } from '@angular/common';
import { ButtonModule } from 'primeng/button';
import { TableModule } from 'primeng/table';
import { SkeletonModule } from 'primeng/skeleton';
import { PanelModule } from 'primeng/panel';
import { TagModule } from 'primeng/tag';
import { CreateManagerDialogComponent } from '../../modules/managers/components/create-manager-dialog/create-manager-dialog.component';
import { AuthApiService , Manager, ManagerStatus } from "fsms-web-api";
import { map } from 'rxjs';
import { AppConfigService } from '../../modules/common/app-config.service';

@Component({
  selector: 'app-managers-page',
  imports: [CommonModule, ButtonModule, TableModule, SkeletonModule, PanelModule, TagModule, CreateManagerDialogComponent],
  templateUrl: './managers.page.html'
})
export class ManagersPage implements OnInit {
  
  private managersQueryService = inject(ManagersQueryService);
  private appConfigService = inject(AppConfigService);
  private messageService = inject(MessageService);
  private authApiService = inject(AuthApiService);

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

  handleSignInAsManager(managerId: number) {
    const managerUrl = this.appConfigService.getConfig().managerUrl;
    this.authApiService.getManagerAccessToken(managerId).subscribe({
        next: (token) => window.open(`${managerUrl}/login?token=${token}`)
      });
  }

  get loading$() {
    return this.managersQueryService.loading$;
  }

}
