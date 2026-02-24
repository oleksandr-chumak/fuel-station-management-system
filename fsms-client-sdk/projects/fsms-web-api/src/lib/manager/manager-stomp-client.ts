import { inject, Injectable } from "@angular/core";
import { map, Observable } from "rxjs";
import { ManagerCreated, ManagerEvent, ManagerEventType, ManagerTerminated } from "./manager-events";
import { ManagerAssignedToFuelStation, ManagerUnassignedFromFuelStation } from "../fuel-station/fuel-station-events";
import { StompClient } from "../core/stomp-client";
import { IMessage } from "@stomp/rx-stomp";

interface IManagerStompClient {
    onManagerCreated(): Observable<ManagerCreated>
    onManagerTerminated(managerId: number): Observable<ManagerTerminated>
    onAll(): Observable<ManagerEvent>
    onAssignedToFuelStation(managerId: number): Observable<ManagerAssignedToFuelStation>
    onUnassignedFromFuelStation(managerId: number): Observable<ManagerUnassignedFromFuelStation>
}

@Injectable({ providedIn: "root" })
export class ManagerStompClient implements IManagerStompClient {

    private readonly stompClient = inject(StompClient);

    onManagerCreated(): Observable<ManagerCreated> {
        return this.stompClient
            .watch({ destination: "/topic/managers/created" })
            .pipe(map(this.parseManagerCreated));
    }

    onManagerTerminated(managerId: number): Observable<ManagerTerminated> {
        return this.stompClient
            .watch({ destination: `/topic/managers/${managerId}/terminated` })
            .pipe(map(this.parseManagerTerminated));
    }

    onAssignedToFuelStation(managerId: number): Observable<ManagerAssignedToFuelStation> {
        return this.stompClient
            .watch({ destination: `/topic/managers/${managerId}/assigned-to-fuel-station` })
            .pipe(map((message) => {
                const json = JSON.parse(message.body);
                return new ManagerAssignedToFuelStation(json.fuelStationId, json.managerId);
            }));
    }

    onUnassignedFromFuelStation(managerId: number): Observable<ManagerUnassignedFromFuelStation> {
        return this.stompClient
            .watch({ destination: `/topic/managers/${managerId}/unassigned-from-fuel-station` })
            .pipe(map((message) => {
                const json = JSON.parse(message.body);
                return new ManagerUnassignedFromFuelStation(json.fuelStationId, json.managerId);
            }));
    }

    onAll(): Observable<ManagerEvent> {
        return this.stompClient
            .watch({ destination: "/topic/managers/**" })
            .pipe(map((message) => {
                const json = JSON.parse(message.body);
                const eventType = json.type as ManagerEventType;

                switch (eventType) {
                    case ManagerEventType.MANAGER_CREATED:
                        return this.parseManagerCreated(message);
                    case ManagerEventType.MANAGER_TERMINATED:
                        return this.parseManagerTerminated(message);
                    default:
                        const _exhaustiveCheck: never = eventType;
                        throw new Error(`Unknown manager event type: ${_exhaustiveCheck}`);
                }
            }));
    }

    private parseManagerCreated(message: IMessage): ManagerCreated {
        const json = JSON.parse(message.body);
        return new ManagerCreated(json.managerId);
    }

    private parseManagerTerminated(message: IMessage): ManagerTerminated {
        const json = JSON.parse(message.body);
        return new ManagerTerminated(json.managerId);
    }

}
