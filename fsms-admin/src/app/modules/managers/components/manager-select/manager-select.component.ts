import { Component, EventEmitter, inject, OnInit, Output } from '@angular/core';
import { Select, SelectChangeEvent } from 'primeng/select';
import { MessageService } from 'primeng/api';
import { finalize } from 'rxjs';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Manager, ManagerApiService } from 'fsms-web-api';

// TODO refactor this
@Component({
  selector: 'app-manager-select',
  imports: [CommonModule ,FormsModule, Select],
  templateUrl: './manager-select.component.html'
})
export class ManagerSelectComponent implements OnInit {
  @Output() managerSelected: EventEmitter<Manager> = new EventEmitter<Manager>();
  private messageService: MessageService = inject(MessageService);
  private managerApiService: ManagerApiService = inject(ManagerApiService);

  loading: boolean = false;
  managers: Manager[] = [];
  selectedManager: Manager | null = null;
  
  ngOnInit(): void {
    this.getManagers();
  }

  handleChange(event: SelectChangeEvent) {
    if(event.value instanceof Manager) {
      this.managerSelected.emit(event.value);
    }
  }

  private getManagers() {
    this.loading = true;
    this.managerApiService.getManagers()
      .pipe(finalize(() => this.loading = false))
      .subscribe({
        next: (data: Manager[]) => {
          this.managers = data;
        },
        error: () => {
          this.messageService.add({ severity: "error", detail: "Error", summary: "An error occurred while fetching managers"});
        }
      })
  }

}
