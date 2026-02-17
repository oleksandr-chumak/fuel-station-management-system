import { Component, inject } from '@angular/core';
import { DialogModule } from 'primeng/dialog';
import { FuelStationFormComponent, FuelStationFormData } from '../fuel-station-form/fuel-station-form.component';
import { tap } from 'rxjs';
import BasicDialog from '../../../common/basic-dialog.component';
import { toSignal } from '@angular/core/rxjs-interop';
import { CreateFuelStationHandler } from '../../handlers/create-fuel-station-handler';

@Component({
  selector: 'app-create-fuel-station-dialog',
  imports: [DialogModule, FuelStationFormComponent],
  templateUrl: './create-fuel-station-dialog.component.html'
})
export class CreateFuelStationDialogComponent extends BasicDialog {
  private readonly createFuelStationHandler = inject(CreateFuelStationHandler);

  readonly loading = toSignal(this.createFuelStationHandler.loading$, { initialValue: false });

  handleFormSubmission({ street, buildingNumber, city, postalCode, country}: FuelStationFormData) {
    this.createFuelStationHandler
      .handle({
        street,
        buildingNumber,
        city,
        postalCode,
        country
      })
      .pipe(tap(() => this.closeDialog()))
      .subscribe()
  }
}
