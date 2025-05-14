import { CommonModule } from '@angular/common';
import { Component, inject, OnInit } from '@angular/core';
import { ButtonModule } from 'primeng/button';
import { DialogModule } from 'primeng/dialog';
import { PanelModule } from 'primeng/panel';
import { SkeletonModule } from 'primeng/skeleton';
import { TableModule } from 'primeng/table';
import { TagModule } from 'primeng/tag';
import { AuthService } from '../../modules/auth/services/auth.service';
import { MessageService } from 'primeng/api';
import { finalize } from 'rxjs';
import { Router } from '@angular/router';
import { FuelStation } from '../../modules/fuel-station/models/fuel-station.model';
import { ManagerApiService } from '../../modules/manager/services/manager-api.service';

@Component({
  selector: 'app-manager-dashboard',
  imports: [CommonModule, PanelModule, TableModule, TagModule, ButtonModule, DialogModule, SkeletonModule],
  templateUrl: './manager-dashboard.component.html',
})
export class ManagerDashboardComponent implements OnInit {

  private authService: AuthService = inject(AuthService);
  private managerApiService: ManagerApiService = inject(ManagerApiService);
  private router: Router = inject(Router);
  private messageService: MessageService = inject(MessageService);

  loading: boolean = false;
  fuelStations: FuelStation[] = [];
  skeletonRows = new Array(5).fill(null);
  skeletonCols = new Array(5).fill(null);

  ngOnInit(): void {
    const user = this.authService.getUserValue();

    if(!user) {
      throw new Error("User not found");
    }

    this.loading = true;
    this.managerApiService.getManagerFuelStations(user.userId)
      .pipe(finalize(() => this.loading = false))
      .subscribe({
        next: (data: FuelStation[]) => {
          this.fuelStations = data;
        }, 
        error: (err: unknown) => {
          console.error("An error occurred while fetching fuel stations", err);
          this.messageService.add({ severity: "error", summary: "Error", detail: "An error occurred while fetching fuel stations"});
        }
      })
  }

  handleViewClick(fuelStationId: number) {
    this.router.navigate(["fuel-station/" + fuelStationId + "/info"]);
  }

  // TODO make this as util
  getSeverity(fuelStation: FuelStation): "success" | undefined {
    if(fuelStation.active) {
      return "success";
    }
    return undefined;
  }

}
