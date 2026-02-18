import { CommonModule } from '@angular/common';
import { Component, inject, OnInit, Signal } from '@angular/core';
import { ButtonModule } from 'primeng/button';
import { DialogModule } from 'primeng/dialog';
import { PanelModule } from 'primeng/panel';
import { SkeletonModule } from 'primeng/skeleton';
import { TableModule } from 'primeng/table';
import { TagModule } from 'primeng/tag';
import { Router } from '@angular/router';
import { FuelStation } from 'fsms-web-api';
import { AuthService } from 'fsms-security';
import { toSignal } from '@angular/core/rxjs-interop';
import { GetManagerFuelStationsHandler } from '../../modules/fuel-station/handlers/get-manager-fuel-stations.handler';

@Component({
  selector: 'app-dashboard-page',
  imports: [CommonModule, PanelModule, TableModule, TagModule, ButtonModule, DialogModule, SkeletonModule],
  templateUrl: './dashboard.page.html',
})
export class DashboardPage implements OnInit {

  private authService = inject(AuthService);
  private handler = inject(GetManagerFuelStationsHandler);
  private router = inject(Router);

  loading: Signal<boolean> = toSignal(this.handler.loading$, { initialValue: false });
  fuelStations: FuelStation[] = [];
  skeletonRows = new Array(5).fill(null);
  skeletonCols = new Array(5).fill(null);

  ngOnInit(): void {
    const user = this.authService.getUserValue();

    if (!user) {
      throw new Error("User not found");
    }

    this.handler.handle({ managerId: user.userId })
      .subscribe({
        next: (data: FuelStation[]) => {
          this.fuelStations = data;
        }
      });
  }

  handleViewClick(fuelStationId: number) {
    this.router.navigate(["fuel-station/" + fuelStationId + "/info"]);
  }

  getSeverity(fuelStation: FuelStation): "success" | undefined {
    if (fuelStation.active) {
      return "success";
    }
    return undefined;
  }

}
