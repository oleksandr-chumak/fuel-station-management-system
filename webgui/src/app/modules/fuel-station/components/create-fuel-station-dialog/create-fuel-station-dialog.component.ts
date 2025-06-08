import { Component, inject } from '@angular/core';
import { DialogModule } from 'primeng/dialog';
import { FuelStationFormComponent } from '../fuel-station-form/fuel-station-form.component';
import FuelStationApiService from '../../services/fuel-station-api.service';
import { finalize } from 'rxjs';
import { MessageService } from 'primeng/api';
import FuelStationQueryService from '../../services/fuel-stations-query.service';
import BasicDialog from '../../../common/basic-dialog.component';
import { FuelStationFormData } from '../../interfaces/fuel-station-form-data.interface';

@Component({
  selector: 'app-create-fuel-station-dialog',
  imports: [DialogModule, FuelStationFormComponent],
  templateUrl: './create-fuel-station-dialog.component.html'
})
export class CreateFuelStationDialogComponent extends BasicDialog {
  private fuelStationQueryService: FuelStationQueryService = inject(FuelStationQueryService);
  private fuelStationApiService: FuelStationApiService = inject(FuelStationApiService);
  private messageService: MessageService = inject(MessageService);

  loading = false;

  handleFormSubmission(e: FuelStationFormData) {
    this.loading = true;
    this.fuelStationApiService.createFuelStation(e.street, e.buildingNumber, e.city, e.postalCode, e.country)
      .pipe(finalize(() => this.loading = false))
      .subscribe({
        next: () => {
          // TODO This logic should be handled inside service 
          this.fuelStationQueryService.getFuelStations()
            .subscribe({
              error: () => this.messageService.add({ severity: "error", summary: "Error", detail: "An error occurred while fetching fuel stations"})
            });
          this.messageService.add({ severity: "success", summary: "Created", detail: "A new fuel station was created"});
          this.closeDialog();
        },
        error: (err) => {
          console.error("Error: ", err);
          this.messageService.add({severity: "error", summary: "Error", detail: "An error occurred while creating fuel station"});
        }
      })
  }
}
