import { inject, Injectable } from '@angular/core';
import { map, Observable } from 'rxjs';

import { ApiService } from '../../common/api/api.service';
import { FuelGrade } from '../../common/api/fuel-grade.enum';

import { FuelOrderMapper } from './fuel-order.mapper';
import { FuelOrder } from './models/fuel-order.model';

@Injectable({ providedIn: 'root'})
export class FuelOrderApiService {
  private apiService = inject(ApiService);
    
  getFuelOrders(): Observable<FuelOrder[]> {
    return this.apiService.get('api/fuel-orders/')
      .pipe(map(data => this.apiService.assertArray(data, FuelOrderMapper.fromJson)));
  } 

  getFuelOrderById(fuelOrderId: number): Observable<FuelOrder> {
    return this.apiService.get(`api/fuel-orders/${fuelOrderId}`)
      .pipe(map(data => FuelOrderMapper.fromJson(data)));
  }
    
  confirmFuelOrder(fuelOrderId: number): Observable<FuelOrder> {
    return this.apiService.put(`api/fuel-orders/${fuelOrderId}/confirm`)
      .pipe(map(data => FuelOrderMapper.fromJson(data)));
  }

  rejectFuelOrder(fuelOrderId: number): Observable<FuelOrder> {
    return this.apiService.put(`api/fuel-orders/${fuelOrderId}/reject`)
      .pipe(map(data => FuelOrderMapper.fromJson(data)));
  }

  createFuelOrder(fuelStationId: number, fuelGrade: FuelGrade, amount: number): Observable<FuelOrder> {
    return this.apiService.post('api/fuel-orders/', { 
      fuelStationId, 
      fuelGrade: this.fuelGradeToSlug(fuelGrade), 
      amount
    })
      .pipe(map(data => FuelOrderMapper.fromJson(data)));
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