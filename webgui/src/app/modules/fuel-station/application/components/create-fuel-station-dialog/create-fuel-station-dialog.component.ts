import { Component, EventEmitter, inject, OnInit, Output } from '@angular/core';
import { MessageService } from 'primeng/api';
import { DialogModule } from 'primeng/dialog';
import { finalize } from 'rxjs';

import { BasicDialog } from '~common/application/basic-dialog.component';
import { FuelStation } from '~fuel-station/api/models/fuel-station.model';
import { FuelStationFormComponent } from '~fuel-station/application/components/fuel-station-form/fuel-station-form.component';
import { FuelStationFormData } from '~fuel-station/application/interfaces/fuel-station-form-data.interface';
import { FuelStationService } from '~fuel-station/application/services/fuel-station.service';

@Component({
  selector: 'app-create-fuel-station-dialog',
  imports: [DialogModule, FuelStationFormComponent],
  templateUrl: './create-fuel-station-dialog.component.html'
})
export class CreateFuelStationDialogComponent extends BasicDialog implements OnInit {
  @Output() fuelStationCreated = new EventEmitter<FuelStation>();

  private fuelStationService: FuelStationService = inject(FuelStationService);
  private messageService: MessageService = inject(MessageService);
  loading = false;

  ngOnInit(): void {
    this.fuelStationService.loading$.subscribe((v) => this.loading = v);
  }

  handleFormSubmission(e: FuelStationFormData) {
    this.fuelStationService.createFuelStation(e.street, e.buildingNumber, e.city, e.postalCode, e.country)
      .pipe(finalize(() => this.loading = false))
      .subscribe({
        next: (data) => {
          this.fuelStationCreated.emit(data);
          this.messageService.add({ severity: 'success', summary: 'Created', detail: 'A new fuel station was created'});
          this.closeDialog();
        },
        error: () => {
          this.messageService.add({severity: 'error', summary: 'Error', detail: 'An error occurred while creating fuel station'});
        }
      });
  }
}
