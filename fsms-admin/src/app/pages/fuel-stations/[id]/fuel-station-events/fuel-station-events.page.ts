import { Component, DestroyRef, inject, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ButtonModule } from 'primeng/button';
import { PanelModule } from 'primeng/panel';
import { SkeletonModule } from 'primeng/skeleton';
import { TableModule } from 'primeng/table';
import { TagModule } from 'primeng/tag';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { finalize } from 'rxjs';
import {
    DomainEventResponse,
    FuelOrderDomainEventResponse,
    FuelPriceChangedEventResponse,
    ManagerAssignedEventResponse
} from 'fsms-web-api';
import { FuelStationStore } from '../../../../modules/fuel-stations/fuel-station-store';
import { GetFuelStationEventsHandler } from '../../../../modules/fuel-stations/handlers/get-fuel-station-events-handler';

@Component({
    selector: 'app-fuel-station-events-page',
    imports: [CommonModule, TagModule, TableModule, PanelModule, SkeletonModule, ButtonModule],
    templateUrl: './fuel-station-events.page.html'
})
export class FuelStationEventsPage implements OnInit {
    private readonly destroyRef = inject(DestroyRef);
    private readonly fuelStationStore = inject(FuelStationStore);
    private readonly getFuelStationEventsHandler = inject(GetFuelStationEventsHandler);

    protected readonly events = signal<DomainEventResponse[]>([]);
    protected readonly loading = signal(false);
    protected readonly loadingMore = signal(false);
    protected readonly hasMore = signal(false);

    protected readonly skeletonRows = new Array(5).fill(null);
    protected readonly skeletonCols = new Array(4).fill(null);

    ngOnInit(): void {
        this.loadEvents();
    }

    private loadEvents(): void {
        const fuelStationId = this.fuelStationStore.fuelStation.fuelStationId;
        this.loading.set(true);
        this.getFuelStationEventsHandler
            .handle({ fuelStationId })
            .pipe(
                finalize(() => this.loading.set(false)),
                takeUntilDestroyed(this.destroyRef)
            )
            .subscribe({
                next: (events) => {
                    this.events.set(events);
                    this.hasMore.set(events.length === 10);
                }
            });
    }

    protected loadMore(): void {
        const current = this.events();
        if (current.length === 0) return;
        const occurredAfter = current[current.length - 1].occurredAt;
        const fuelStationId = this.fuelStationStore.fuelStation.fuelStationId;
        this.loadingMore.set(true);
        this.getFuelStationEventsHandler
            .handle({ fuelStationId, occurredAfter })
            .pipe(
                finalize(() => this.loadingMore.set(false)),
                takeUntilDestroyed(this.destroyRef)
            )
            .subscribe({
                next: (newEvents) => {
                    this.events.update(prev => [...prev, ...newEvents]);
                    this.hasMore.set(newEvents.length === 10);
                }
            });
    }

    protected eventLabel(type: string): string {
        switch (type) {
            case 'FUEL_STATION_CREATED':                 return 'Station Created';
            case 'FUEL_STATION_DEACTIVATED':             return 'Station Deactivated';
            case 'FUEL_STATION_FUEL_PRICE_CHANGED':      return 'Price Changed';
            case 'MANAGER_ASSIGNED_TO_FUEL_STATION':     return 'Manager Assigned';
            case 'MANAGER_UNASSIGNED_FROM_FUEL_STATION': return 'Manager Unassigned';
            case 'FUEL_ORDER_CREATED':                   return 'Order Created';
            case 'FUEL_ORDER_CONFIRMED':                 return 'Order Confirmed';
            case 'FUEL_ORDER_REJECTED':                  return 'Order Rejected';
            case 'FUEL_ORDER_PROCESSED':                 return 'Order Processed';
            default:                                     return type;
        }
    }

    protected eventSeverity(type: string): 'success' | 'info' | 'warn' | 'danger' | undefined {
        switch (type) {
            case 'FUEL_STATION_CREATED':                 return 'success';
            case 'FUEL_STATION_DEACTIVATED':             return 'danger';
            case 'FUEL_STATION_FUEL_PRICE_CHANGED':      return 'info';
            case 'MANAGER_ASSIGNED_TO_FUEL_STATION':     return 'success';
            case 'MANAGER_UNASSIGNED_FROM_FUEL_STATION': return 'warn';
            case 'FUEL_ORDER_CREATED':                   return 'info';
            case 'FUEL_ORDER_CONFIRMED':                 return 'success';
            case 'FUEL_ORDER_REJECTED':                  return 'danger';
            case 'FUEL_ORDER_PROCESSED':                 return 'success';
            default:                                     return undefined;
        }
    }

    protected eventDetails(event: DomainEventResponse): string {
        switch (event.type) {
            case 'FUEL_STATION_FUEL_PRICE_CHANGED': {
                const e = event as FuelPriceChangedEventResponse;
                return `${e.fuelGrade}: ${e.pricePerLiter}/L`;
            }
            case 'MANAGER_ASSIGNED_TO_FUEL_STATION':
            case 'MANAGER_UNASSIGNED_FROM_FUEL_STATION': {
                const e = event as ManagerAssignedEventResponse;
                return `${e.manager.firstName} ${e.manager.lastName}`;
            }
            case 'FUEL_ORDER_CREATED':
            case 'FUEL_ORDER_CONFIRMED':
            case 'FUEL_ORDER_REJECTED':
            case 'FUEL_ORDER_PROCESSED': {
                const e = event as FuelOrderDomainEventResponse;
                return `Order #${e.fuelOrderId}`;
            }
            default:
                return '—';
        }
    }

    protected formatDate(iso: string): string {
        return new Date(iso).toLocaleString('en-GB', {
            day: '2-digit', month: 'short', year: 'numeric',
            hour: '2-digit', minute: '2-digit'
        });
    }
}
