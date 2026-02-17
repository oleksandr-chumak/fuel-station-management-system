import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ButtonModule } from 'primeng/button';
import { TableModule } from 'primeng/table';
import { SkeletonModule } from 'primeng/skeleton';
import { PanelModule } from 'primeng/panel';
import { TagModule } from 'primeng/tag';
import { CreateManagerDialogComponent } from '../../modules/managers/components/create-manager-dialog/create-manager-dialog.component';
import { Manager, ManagerStatus } from "fsms-web-api";
import { AppConfigService } from '../../modules/common/app-config.service';
import { GetManagersHandler } from '../../modules/managers/commands/get-managers-handler';
import { GetManagerAccessTokenHandler } from '../../modules/managers/commands/get-manager-access-token-handler';

@Component({
  selector: 'app-managers-page',
  imports: [CommonModule, ButtonModule, TableModule, SkeletonModule, PanelModule, TagModule, CreateManagerDialogComponent],
  templateUrl: './managers.page.html'
})
export class ManagersPage implements OnInit {
  
  private appConfigService = inject(AppConfigService);
  private getManagersHandler = inject(GetManagersHandler);
  private getManagerAccessTokenHandler = inject(GetManagerAccessTokenHandler);

  managers: Manager[] = [];
  loading$ = this.getManagersHandler.loading$;

  skeletonRows = new Array(5).fill(null);
  skeletonCols = new Array(6).fill(null);
  
  ngOnInit(): void {
    this.getManagersHandler.handle({})
      .subscribe((managers) => this.managers = managers)
  }

  handleSignInAsManager(managerId: number) {
    const managerUrl = this.appConfigService.getConfig().managerUrl;
    this.getManagerAccessTokenHandler
      .handle({ managerId })
      .subscribe(token => window.open(`${managerUrl}/login?token=${token}`));
  }

  getSeverity(manager: Manager): "success" | undefined {
    if(manager.status === ManagerStatus.Active) {
      return "success";
    }
    return undefined;
  }
}
