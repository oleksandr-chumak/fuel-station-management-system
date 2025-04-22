import { Component, inject, Input } from '@angular/core';
import { DialogModule } from 'primeng/dialog';
import { FuelStationFormComponent } from '../fuel-station-form/fuel-station-form.component';
import { FuelStationFormData } from '../fuel-station.model';
import FuelStationApiService from '../../infrastructure/fuel-station-api.service';
import { finalize } from 'rxjs';
import { MessageService } from 'primeng/api';
import FuelStationQueryService from '../fuel-stations-query.service';

@Component({
  selector: 'app-create-fuel-station-dialog',
  imports: [DialogModule, FuelStationFormComponent],
  templateUrl: './create-fuel-station-dialog.component.html'
})
export class CreateFuelStationDialogComponent {
  @Input() visible: boolean = false;

  private fuelStationQueryService: FuelStationQueryService = inject(FuelStationQueryService);
  private fuelStationApiService: FuelStationApiService = inject(FuelStationApiService);
  private messageService: MessageService = inject(MessageService);

  loading = false;

  handleFormSubmission(e: FuelStationFormData) {
    this.loading = true;
    console.log(e)
    this.fuelStationApiService.createFuelStation(e.street, e.buildingNumber, e.city, e.postalCode, e.country)
      .pipe(finalize(() => this.loading = false))
      .subscribe({
        next: () => {
          this.fuelStationQueryService.getFuelStations();
          this.messageService.add({ severity: "success", summary: "Created", detail: "A new fuel station was created"});
          this.visible = false;
        },
        error: (err) => {
          console.error("Error: ", err);
          this.messageService.add({severity: "error", summary: "Error", detail: "An error occurred while creating fuel station"});
        }
      })
  }
}
