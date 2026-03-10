# fsms-web-api

Angular library providing typed REST and STOMP WebSocket clients for all domains of the Fuel Station Management System API.

## Setup

Register the module in your app's providers with a configuration token:

```typescript
import { WebApiModule, WEB_API_CONFIG } from 'fsms-web-api';

// app.config.ts
export const appConfig: ApplicationConfig = {
  providers: [
    importProvidersFrom(WebApiModule),
    {
      provide: WEB_API_CONFIG,
      useFactory: (appConfigService: AppConfigService) => ({
        restApiUrl: appConfigService.restApiUrl,
        stompApiUrl: appConfigService.stompApiUrl,
      }),
      deps: [AppConfigService],
    },
  ],
};
```

## Clients Reference

### Core

| Service | Description |
|---|---|
| `RestClient` | Base HTTP client wrapping Angular `HttpClient` |
| `StompClient` | Base STOMP client wrapping `@stomp/rx-stomp` |

### Fuel Stations

```typescript
import { FuelStationRestClient, FuelStationStompClient } from 'fsms-web-api';
```

**`FuelStationRestClient`**

| Method | Description |
|---|---|
| `getFuelStations()` | List all fuel stations |
| `getFuelStation(id)` | Get fuel station by ID |
| `createFuelStation(req)` | Create a new fuel station |
| `deactivateFuelStation(id)` | Deactivate a fuel station |
| `assignManager(id, req)` | Assign manager to station |
| `unassignManager(id, req)` | Unassign manager from station |
| `changeFuelPrice(id, req)` | Update fuel price |
| `getFuelStationManagers(id)` | List managers for a station |
| `getFuelStationFuelOrders(id)` | List fuel orders for a station |
| `getFuelStationEvents(id, occurredAfter?)` | Get event timeline (cursor paginated) |

**`FuelStationStompClient`**

| Method | Topic | Description |
|---|---|---|
| `onFuelStationEvents(id)` | `/topic/fuel-stations/{id}/**` | All events for a station |
| `onPriceChanged(id)` | `/topic/fuel-stations/{id}/price-changed` | Price updated |
| `onManagerAssigned(id)` | `/topic/fuel-stations/{id}/manager-assigned` | Manager assigned |
| `onManagerUnassigned(id)` | `/topic/fuel-stations/{id}/manager-unassigned` | Manager unassigned |

### Fuel Orders

```typescript
import { FuelOrderRestClient, FuelOrderStompClient } from 'fsms-web-api';
```

**`FuelOrderRestClient`** — `getFuelOrders()`, `getFuelOrder(id)`, `createFuelOrder(req)`, `confirmFuelOrder(id)`, `rejectFuelOrder(id)`

**`FuelOrderStompClient`** — `onFuelOrderEvents(id)` → `/topic/fuel-orders/{id}/**`

### Managers

```typescript
import { ManagerRestClient, ManagerStompClient } from 'fsms-web-api';
```

**`ManagerRestClient`** — `getManagers()`, `getManager(id)`, `createManager(req)`, `terminateManager(id)`, `getManagerFuelStations(id)`

**`ManagerStompClient`**

| Method | Topic | Description |
|---|---|---|
| `onAssignedToFuelStation(id)` | `/topic/managers/{id}/assigned-to-fuel-station` | Manager received assignment |
| `onUnassignedFromFuelStation(id)` | `/topic/managers/{id}/unassigned-from-fuel-station` | Manager lost assignment |

### Auth

```typescript
import { AuthRestClient } from 'fsms-web-api';
```

`AuthRestClient` — `loginAdmin(req)`, `loginManager(req)`, `getManagerToken(managerId)`, `getMe()`

## Models & Types

| Type | Description |
|---|---|
| `FuelGrade` | Enum: `PETROL`, `DIESEL`, etc. |
| `FuelStation` | Fuel station entity |
| `FuelStationStatus` | Enum: `ACTIVE`, `INACTIVE` |
| `FuelPrice` | Price per fuel grade |
| `FuelTank` | Tank with grade and level |
| `FuelOrder` | Fuel order entity |
| `FuelOrderStatus` | Enum: `PENDING`, `CONFIRMED`, `REJECTED` |
| `Manager` | Manager entity |
| `ManagerStatus` | Enum: `ACTIVE`, `TERMINATED` |
| `CursorPage<T>` | Cursor-paginated response |
| `DomainEventResponse` | Base type for domain events |
| `FuelPriceChangedEventResponse` | Price change event |
| `ManagerAssignedEventResponse` | Manager assigned event |
| `ManagerUnassignedEventResponse` | Manager unassigned event |
| `FuelOrderDomainEventResponse` | Fuel order event (adds `fuelOrderId`) |
| `User` | Authenticated user info |
| `UserRole` | Enum: `ADMINISTRATOR`, `MANAGER` |
| `WebApiConfig` | Configuration interface |

## Usage Example

```typescript
// Subscribe to STOMP events
@Component({ ... })
export class FuelStationPage implements OnInit {
  constructor(
    private fuelStationRestClient: FuelStationRestClient,
    private fuelStationStompClient: FuelStationStompClient,
    private destroyRef: DestroyRef,
  ) {}

  ngOnInit(): void {
    // Load initial data
    this.fuelStationRestClient.getFuelStation(this.id).subscribe(station => {
      // handle station
    });

    // Subscribe to real-time price changes
    this.fuelStationStompClient.onPriceChanged(this.id)
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe(event => {
        // handle event
      });
  }
}
```
