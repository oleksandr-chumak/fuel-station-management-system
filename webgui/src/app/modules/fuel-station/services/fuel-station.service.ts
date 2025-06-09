import { inject, Injectable } from '@angular/core';
import { BehaviorSubject, finalize, Observable } from 'rxjs';

import { FuelStationApiService } from './fuel-station-api.service';

@Injectable({ providedIn: 'root'})
export class FuelStationService {
  private fuelStationApiService: FuelStationApiService = inject(FuelStationApiService);

  private loading = new BehaviorSubject(false);
  loading$ = this.loading.asObservable();

  //TODO make as util or smth like that 
  private withLoading<T>(observable: Observable<T>): Observable<T> {
    this.loading.next(true);
    return observable.pipe(finalize(() => this.loading.next(false)));
  }

  getFuelStations() {
    return this.withLoading(this.fuelStationApiService.getFuelStations());
  }

  createFuelStation(street: string, buildingNumber: string, city: string, postalCode: string, country: string) {
    return this.withLoading(this.fuelStationApiService.createFuelStation(street, buildingNumber, city, postalCode, country));
  }
}