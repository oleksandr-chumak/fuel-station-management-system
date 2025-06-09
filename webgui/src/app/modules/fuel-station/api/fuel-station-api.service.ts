import { inject, Injectable } from '@angular/core';
import { map, Observable } from 'rxjs';

import { FuelStationMapper } from './fuel-station.mapper';
import { FuelStation } from './models/fuel-station.model';

import { ApiService } from '~common/api/api.service';
import { FuelGrade } from '~common/api/fuel-grade.enum';
import { FuelOrderMapper } from '~fuel-order/api/fuel-order.mapper';
import { FuelOrder } from '~fuel-order/api/models/fuel-order.model';
import { ManagerMapper } from '~manager/api/manager.mapper';
import { Manager } from '~manager/api/models/manager.model';

@Injectable({ providedIn: 'root' })
export class FuelStationApiService {
  private apiService = inject(ApiService);
    
  getFuelStationOrders(fuelStationId: number): Observable<FuelOrder[]> {
    return this.apiService.get(`api/fuel-stations/${fuelStationId}/fuel-orders`)
      .pipe(map((data) => this.apiService.assertArray(data, (data) => FuelOrderMapper.fromJson(data))));
  }

  getAssignedManagers(fuelStationId: number): Observable<Manager[]> {
    return this.apiService.get(`api/fuel-stations/${fuelStationId}/managers`)
      .pipe(map(data => this.apiService.assertArray(data, (data) => ManagerMapper.fromJson(data))));
  }

  getFuelStations(): Observable<FuelStation[]> {
    return this.apiService.get('api/fuel-stations/')
      .pipe(map(data => this.apiService.assertArray(data, (data) => FuelStationMapper.fromJson(data))));
  }
    
  getFuelStationById(fuelStationId: number): Observable<FuelStation> {
    return this.apiService.get(`api/fuel-stations/${fuelStationId}`)
      .pipe(map(data => FuelStationMapper.fromJson(data)));
  }

  changeFuelPrice(fuelStationId: number, fuelGrade: FuelGrade, newFuelPrice: number): Observable<FuelStation> {
    return this.apiService.put(`api/fuel-stations/${fuelStationId}/change-fuel-price`, 
      { fuelGrade: this.fuelGradeToSlug(fuelGrade), newPrice: newFuelPrice })
      .pipe(map(data => FuelStationMapper.fromJson(data)));
  }

  unassignManager(fuelStationId: number, managerId: number): Observable<FuelStation> {
    return this.apiService.put(`api/fuel-stations/${fuelStationId}/unassign-manager`, { managerId })
      .pipe(map(data => FuelStationMapper.fromJson(data)));
  }

  assignManager(fuelStationId: number, managerId: number): Observable<FuelStation> {
    return this.apiService.put(`api/fuel-stations/${fuelStationId}/assign-manager`, { managerId })
      .pipe(map(data => FuelStationMapper.fromJson(data)));
  }

  deactivateFuelStation(fuelStationId: number): Observable<FuelStation> {
    return this.apiService.put(`api/fuel-stations/${fuelStationId}/deactivate`)
      .pipe(map(data => FuelStationMapper.fromJson(data)));
  }

  createFuelStation(street: string, buildingNumber: string, city: string, postalCode: string, country: string): Observable<FuelStation> {
    return this.apiService.post('api/fuel-stations/', { street, buildingNumber, city, postalCode, country })
      .pipe(map(data => FuelStationMapper.fromJson(data)));
  }

  private fuelGradeToSlug(fuelGrade: FuelGrade): string {
    switch(fuelGrade) {
    case FuelGrade.Diesel:
      return 'diesel';
    case FuelGrade.RON_92:
      return 'ron-92';
    case FuelGrade.RON_95:
      return 'ron-95';
    default:
      throw new Error(`Cannot transform FuelGrade: ${fuelGrade} to slug`);
    }
  }
}