import { Component, inject, OnDestroy, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ButtonModule } from 'primeng/button';
import { PanelModule } from 'primeng/panel';
import { SkeletonModule } from 'primeng/skeleton';
import { TableModule, TablePageEvent } from 'primeng/table';
import { TagModule } from 'primeng/tag';
import { toSignal } from '@angular/core/rxjs-interop';
import { FuelStationEventsStore } from '../../../../modules/fuel-stations/stores/fuel-station-events-store';

@Component({
    selector: 'app-fuel-station-events-page',
    imports: [CommonModule, TagModule, TableModule, PanelModule, SkeletonModule, ButtonModule],
    templateUrl: './fuel-station-events.page.html',
    styles: [`
        :host ::ng-deep .p-paginator {
            justify-content: flex-end;
        }
        :host ::ng-deep .p-panel-header {
            padding: 0;
        }
    `]
})
export class FuelStationEventsPage implements OnInit, OnDestroy {
    protected readonly fuelStationEventsStore = inject(FuelStationEventsStore);

    protected readonly events = toSignal(this.fuelStationEventsStore.currentPageEvents$, { initialValue: [] });
    protected readonly loading = toSignal(this.fuelStationEventsStore.loading$, { initialValue: false });
    protected readonly totalEvents = toSignal(this.fuelStationEventsStore.totalEvents$, { initialValue: 0 });
    protected readonly paginationRange = this.fuelStationEventsStore.paginationRange;
    protected readonly eventsPerPage = this.fuelStationEventsStore.eventsPerPage;

    protected readonly skeletonRows = new Array(this.eventsPerPage).fill(null);
    protected readonly skeletonCols = new Array(4).fill(null);

    ngOnInit(): void {
        this.fuelStationEventsStore.fetchEvents();
    }

    ngOnDestroy(): void {
        this.fuelStationEventsStore.reset();
    }

    protected onPageChange(event: TablePageEvent): void {
        if(this.loading()) {
            return;
        }
        this.fuelStationEventsStore.changePage(Math.ceil(event.first / event.rows) + 1);
    }

    protected eventLabel(type: string): string {
        switch (type) {
            case 'FUEL_STATION_CREATED': return 'Station Created';
            case 'FUEL_STATION_DEACTIVATED': return 'Station Deactivated';
            case 'FUEL_STATION_FUEL_PRICE_CHANGED': return 'Price Changed';
            case 'MANAGER_ASSIGNED_TO_FUEL_STATION': return 'Manager Assigned';
            case 'MANAGER_UNASSIGNED_FROM_FUEL_STATION': return 'Manager Unassigned';
            case 'FUEL_ORDER_CREATED': return 'Order Created';
            case 'FUEL_ORDER_CONFIRMED': return 'Order Confirmed';
            case 'FUEL_ORDER_REJECTED': return 'Order Rejected';
            case 'FUEL_ORDER_PROCESSED': return 'Order Processed';
            default: return type;
        }
    }

    protected eventSeverity(type: string): 'success' | 'info' | 'warn' | 'danger' | undefined {
        switch (type) {
            case 'FUEL_STATION_CREATED': return 'success';
            case 'FUEL_STATION_DEACTIVATED': return 'danger';
            case 'FUEL_STATION_FUEL_PRICE_CHANGED': return 'info';
            case 'MANAGER_ASSIGNED_TO_FUEL_STATION': return 'success';
            case 'MANAGER_UNASSIGNED_FROM_FUEL_STATION': return 'warn';
            case 'FUEL_ORDER_CREATED': return 'info';
            case 'FUEL_ORDER_CONFIRMED': return 'success';
            case 'FUEL_ORDER_REJECTED': return 'danger';
            case 'FUEL_ORDER_PROCESSED': return 'success';
            default: return undefined;
        }
    }

    protected formatDate(iso: string): string {
        return new Date(iso).toLocaleString('en-GB', {
            day: '2-digit', month: 'short', year: 'numeric',
            hour: '2-digit', minute: '2-digit'
        });
    }
}
