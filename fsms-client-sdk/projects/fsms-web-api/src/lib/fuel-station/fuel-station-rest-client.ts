import { inject, Injectable } from "@angular/core";
import { map, Observable } from "rxjs";
import { RestClient } from "../core/rest-client";
import { CountryCode } from "./country-code.enum";
import { FuelStationMapper } from "./fuel-station.mapper";
import { ManagerMapper } from "../manager/manager.mapper";
import { FuelOrderMapper } from "../fuel-order/fuel-order.mapper";
import { FuelOrder } from "../fuel-order/fuel-order.model";
import { Manager } from "../manager/manager.model";
import { FuelStation } from "./fuel-station.model";
import { FuelGrade } from "../core/fuel-grade.enum";
import { DomainEventResponse } from "./fuel-station-event-response";
import { CursorPage } from "../core/cursor-page";
import { FuelPurchase } from "./fuel-purchase.model";
import { FuelSale } from "./fuel-sale.model";
import { FuelStationFuelPriceHistoryEntry } from "./fuel-price-history";

@Injectable({ providedIn: "root" })
export class FuelStationRestClient {
    
    private restClient = inject(RestClient);
    private fuelStationMapper = inject(FuelStationMapper);
    private managerMapper = inject(ManagerMapper);
    private fuelOrderMapper = inject(FuelOrderMapper);
    
    getFuelStationOrders(fuelStationId: number): Observable<FuelOrder[]> {
        return this.restClient.get(`api/fuel-stations/${fuelStationId}/fuel-orders`)
            .pipe(map((data) => this.restClient.assertArray(data, this.fuelOrderMapper.fromJson.bind(this.fuelOrderMapper))));
    }

    getAssignedManagers(fuelStationId: number): Observable<Manager[]> {
        return this.restClient.get(`api/fuel-stations/${fuelStationId}/managers`)
            .pipe(map(data => this.restClient.assertArray(data, this.managerMapper.fromJson.bind(this.managerMapper))));
    }

    getFuelStations(): Observable<FuelStation[]> {
        return this.restClient.get("api/fuel-stations/")
            .pipe(map(data => this.restClient.assertArray(data, this.fuelStationMapper.fromJson.bind(this.fuelStationMapper))));
    }
    
    getFuelStationById(fuelStationId: number): Observable<FuelStation> {
        return this.restClient.get(`api/fuel-stations/${fuelStationId}`)
            .pipe(map(data => this.fuelStationMapper.fromJson(data)));
    }

    changeFuelPrice(fuelStationId: number, fuelGrade: FuelGrade, newFuelPrice: number): Observable<FuelStation> {
        return this.restClient.put(`api/fuel-stations/${fuelStationId}/fuel-prices/${this.fuelGradeToSlug(fuelGrade)}`,
            { newPrice: newFuelPrice })
            .pipe(map(data => this.fuelStationMapper.fromJson(data)));
    }

    updateFuelPrices(fuelStationId: number, prices: { fuelGrade: FuelGrade, newPrice: number }[]): Observable<FuelStation> {
        return this.restClient.put(`api/fuel-stations/${fuelStationId}/fuel-prices`, {
            prices: prices.map(p => ({ fuelGrade: this.fuelGradeToSlug(p.fuelGrade), newPrice: p.newPrice })),
        })
            .pipe(map(data => this.fuelStationMapper.fromJson(data)));
    }

    // TODO Change unassign-manager to managers/{managerId}/unassign
    unassignManager(fuelStationId: number, managerId: number): Observable<Manager> {
        return this.restClient.put(`api/fuel-stations/${fuelStationId}/unassign-manager`, { managerId })
            .pipe(map(data => this.managerMapper.fromJson(data)));
    }

    // TODO Change assign-manager to managers/{managerId}/assign
    assignManager(fuelStationId: number, managerId: number): Observable<Manager> {
        return this.restClient.put(`api/fuel-stations/${fuelStationId}/assign-manager`, { managerId })
            .pipe(map(data => this.managerMapper.fromJson(data)));
    }

    deactivateFuelStation(fuelStationId: number): Observable<FuelStation> {
        return this.restClient.put(`api/fuel-stations/${fuelStationId}/deactivate`)
            .pipe(map(data => this.fuelStationMapper.fromJson(data)));
    }

    dispenseFuel(fuelStationId: number, fuelTankId: number, volume: number): Observable<FuelStation> {
        return this.restClient
            .post(`api/fuel-stations/${fuelStationId}/fuel-tanks/${fuelTankId}/dispense`, { volume })
            .pipe(map(data => this.fuelStationMapper.fromJson(data)));
    }

    decommissionFuelTank(fuelStationId: number, fuelTankId: number): Observable<FuelStation> {
        return this.restClient
            .put(`api/fuel-stations/${fuelStationId}/fuel-tanks/${fuelTankId}/decommission`)
            .pipe(map(data => this.fuelStationMapper.fromJson(data)));
    }

    installFuelTank(fuelStationId: number, fuelGrade: FuelGrade, maxCapacity: number): Observable<FuelStation> {
        return this.restClient
            .post(`api/fuel-stations/${fuelStationId}/fuel-tanks`, {
                fuelGrade: this.fuelGradeToSlug(fuelGrade),
                maxCapacity,
            })
            .pipe(map(data => this.fuelStationMapper.fromJson(data)));
    }

    getFuelStationEvents(fuelStationId: number, limit?: number, occurredAfter?: string): Observable<CursorPage<DomainEventResponse, string>> {
        const params = { occurredAfter, limit };
        (Object.keys(params) as Array<keyof typeof params>)
            .forEach(key => params[key] === undefined && delete params[key]);

        return this.restClient.get<CursorPage<DomainEventResponse, string>>(
            `api/fuel-stations/${fuelStationId}/events`, { params }
        );
    }

    getFuelStationFuelPurchases(fuelStationId: number): Observable<FuelPurchase[]> {
        return this.restClient.get<FuelPurchase[]>(`api/fuel-stations/${fuelStationId}/fuel-purchases`);
    }

    getFuelStationFuelSales(fuelStationId: number): Observable<FuelSale[]> {
        return this.restClient.get<FuelSale[]>(`api/fuel-stations/${fuelStationId}/fuel-sales`);
    }

    getFuelPriceHistory(fuelStationId: number): Observable<FuelStationFuelPriceHistoryEntry[]> {
        return this.restClient.get<FuelStationFuelPriceHistoryEntry[]>(
            `api/fuel-stations/${fuelStationId}/fuel-price-history`
        );
    }

    createFuelStation(street: string, buildingNumber: string, city: string, postalCode: string, country: CountryCode): Observable<FuelStation> {
        return this.restClient.post("api/fuel-stations/", { street, buildingNumber, city, postalCode, country })
            .pipe(map(data => this.fuelStationMapper.fromJson(data)));
    }

    private fuelGradeToSlug(fuelGrade: FuelGrade): string {
        switch(fuelGrade) {
            case FuelGrade.Diesel:
                return "diesel";
            case FuelGrade.RON_92:
                return "ron-92";
            case FuelGrade.RON_95:
                return "ron-95";
            default:
                throw new Error(`Cannot transform FuelGrade: ${fuelGrade} to slug`);
        }
    }
}