import { Component, EventEmitter, inject, OnInit, Output } from '@angular/core';
import { Select, SelectChangeEvent } from 'primeng/select';
import { tap } from 'rxjs';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Manager } from 'fsms-web-api';
import { GetManagersHandler } from '../../handlers/get-managers-handler';
import { toSignal } from '@angular/core/rxjs-interop';

@Component({
  selector: 'app-manager-select',
  imports: [CommonModule ,FormsModule, Select],
  templateUrl: './manager-select.component.html'
})
export class ManagerSelectComponent implements OnInit {
  @Output() managerSelected: EventEmitter<Manager> = new EventEmitter<Manager>();
  private readonly getManagersHandler = inject(GetManagersHandler);

  loading = toSignal(this.getManagersHandler.loading$, {initialValue: false});
  managers: Manager[] = [];
  selectedManager: Manager | null = null;
  
  ngOnInit(): void {
    this.getManagersHandler
      .handle({})
      .pipe(tap((managers) => this.managers = managers))
      .subscribe()
  }

  handleChange(event: SelectChangeEvent) {
    if(event.value instanceof Manager) {
      this.managerSelected.emit(event.value);
    }
  }

}
