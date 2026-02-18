import { inject, Injectable } from "@angular/core";
import { map, Observable } from "rxjs";
import { ManagerCreated, ManagerEvent, ManagerTerminated } from "./manager-events";
import { StompClient } from "../core/stomp-client";
import { IMessage } from "@stomp/rx-stomp";

interface IManagerStompClient {
    onManagerCreated(): Observable<ManagerCreated>
    onManagerTerminated(managerId: number): Observable<ManagerTerminated>
    onAll(): Observable<ManagerEvent> 
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

    // TODO
    onAll(): Observable<ManagerEvent> {
        return this.stompClient
            .watch({ destination: "/topic/managers/**" })
            .pipe(map(this.parseManagerTerminated));
    }

    private parseManagerCreated(message: IMessage): ManagerCreated {
        const json = JSON.parse(message.body);
        return new ManagerCreated(json.credentialsId);
    }

    private parseManagerTerminated(message: IMessage): ManagerTerminated {
        const json = JSON.parse(message.body);
        return new ManagerTerminated(json.mangerId);
    }

}
