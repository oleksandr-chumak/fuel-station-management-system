import { inject, Injectable } from '@angular/core';
import { ManagerApiService } from './manager-api.service';
import { BehaviorSubject, finalize, Observable } from 'rxjs';
import Manager from '../models/manager.model';

@Injectable({ providedIn: 'root' })
export default class ManagerService {
  private managerApiService: ManagerApiService = inject(ManagerApiService);

  private loading = new BehaviorSubject(false);
  loading$ = this.loading.asObservable();

  //TODO make as util or smth like that 
  private withLoading<T>(observable: Observable<T>): Observable<T> {
    this.loading.next(true);
    return observable.pipe(finalize(() => this.loading.next(false)))
  }

  getManagers() {
    return this.withLoading(this.managerApiService.getManagers())
  }
  
  createManager(firstName: string, lastName: string, email: string) {
    return this.withLoading(this.managerApiService.createManager(firstName, lastName, email))
  }

  terminate(manager: Manager): Observable<Manager> {
    return this.withLoading(this.managerApiService.terminateManager(manager.id))
  }
}