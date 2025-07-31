import { CommonModule } from '@angular/common';
import { Component, inject, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MessageService } from 'primeng/api';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { PanelModule } from 'primeng/panel';
import { SkeletonModule } from 'primeng/skeleton';
import { TableModule } from 'primeng/table';

import { FuelGrade } from '~common/api/fuel-grade.enum';
import { FuelPrice } from '~fuel-station/api/models/fuel-price.model';
import { AdminFuelStationContextLoadingEvent } from '~fuel-station/application/models/admin-fuel-station-context-loading-event.enum';
import { AdminFuelStationContextService } from '~fuel-station/application/services/admin-fuel-station-context.service';

@Component({
  selector: 'app-admin-fuel-station-fuel-prices',
  imports: [CommonModule, TableModule, InputTextModule, SkeletonModule, PanelModule, FormsModule, ButtonModule],
  templateUrl: './admin-fuel-station-fuel-prices.component.html'
})
export class AdminFuelStationFuelPricesComponent implements OnInit {
  private messageService: MessageService = inject(MessageService);
  private ctxService: AdminFuelStationContextService = inject(AdminFuelStationContextService);

  clonedFuelPrices = this.ctxService.getContextValue()?.fuelStation.clone().fuelPrices || [];
  skeletonRows = new Array(5).fill(null);
  skeletonCols = new Array(3).fill(null);
  loading = false;
  changeFuelPriceLoading = false;

  ngOnInit(): void {
    this.ctx$.subscribe((data) => {
      this.clonedFuelPrices = data?.fuelStation.clone().fuelPrices || [];
    });
    this.ctxService.loadingEvents$.subscribe((event) => {
      if(event?.type === AdminFuelStationContextLoadingEvent.GET_FUEL_STATION) {
        this.loading = event.value;
      } else if(event?.type === AdminFuelStationContextLoadingEvent.CHANGE_FUEL_PRICE) {
        this.changeFuelPriceLoading = false;
      }
    });
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
