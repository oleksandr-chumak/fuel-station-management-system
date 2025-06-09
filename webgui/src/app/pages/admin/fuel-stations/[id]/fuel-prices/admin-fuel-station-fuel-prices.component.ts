import { CommonModule } from '@angular/common';
import { Component, inject, OnInit } from '@angular/core';
import { InputTextModule } from 'primeng/inputtext';
import { PanelModule } from 'primeng/panel';
import { SkeletonModule } from 'primeng/skeleton';
import { TableModule } from 'primeng/table';
import AdminFuelStationContextService from '../../../../../modules/fuel-station/services/admin-fuel-station-context.service';
import FuelGrade from '../../../../../modules/common/fuel-grade.enum';
import { FormsModule } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { MessageService } from 'primeng/api';
import FuelPrice from '../../../../../modules/fuel-station/models/fuel-price.model';

@Component({
  selector: 'app-admin-fuel-station-fuel-prices',
  imports: [CommonModule, TableModule, InputTextModule, SkeletonModule, PanelModule, FormsModule, ButtonModule],
  templateUrl: './admin-fuel-station-fuel-prices.component.html'
})
export class AdminFuelStationFuelPricesComponent implements OnInit {
  private messageService: MessageService = inject(MessageService);
  private ctxService: AdminFuelStationContextService = inject(AdminFuelStationContextService);

  loading = false;
  clonedFuelPrices = this.ctxService.getContextValue()?.fuelStation.clone().fuelPrices || [];
  skeletonRows = new Array(5).fill(null);
  skeletonCols = new Array(3).fill(null);

  ngOnInit(): void {
    this.ctx$.subscribe((data) => {
      this.clonedFuelPrices = data?.fuelStation.clone().fuelPrices || [];
    })
    this.ctxService.loading$.subscribe((value) => this.loading = value);
  }

  getFuelGradeValue(fuelGrade: FuelGrade) {
    return FuelGrade[fuelGrade];
  }

  onRowEditInit() {
    this.resetFuelPrices();
  }

  onRowEditSave(fuelPrice: FuelPrice) {
    const newFuelPrice = Number(fuelPrice.pricePerLiter);
    if(Number.isNaN(newFuelPrice)) {
      this.resetFuelPrices();
      this.messageService.add({ severity: 'error', summary: 'Validation', detail: 'Fuel price must be a number'});
      return;
    }

    this.ctxService.changeFuelPrice(fuelPrice.fuelGrade, newFuelPrice).subscribe({
      error: () => {
        this.resetFuelPrices();
        this.messageService.add({ severity: 'error', summary: 'Error', detail: 'An error occurred while fetching fuel orders'});
      },
      next: () => {
        this.messageService.add({ severity: 'success', summary: 'Changed', detail: 'Fuel price was successfully changed' });
      }
    });
  }

  onRowEditCancel() {
    this.resetFuelPrices();
  }

  private resetFuelPrices() {
    this.clonedFuelPrices = this.ctxService.getContextValue()?.fuelStation.clone().fuelPrices || [];
  }

  get ctx$() {
    return this.ctxService.getContext();
  }
}
