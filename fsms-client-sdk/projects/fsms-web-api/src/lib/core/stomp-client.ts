import { inject, Injectable } from "@angular/core";
import { IMessage, IWatchParams, RxStomp } from "@stomp/rx-stomp";
import { Observable } from "rxjs";
import { WEB_API_CONFIG } from "./web-api-config.interface";

interface IStompClient {
    watch(opts: IWatchParams): Observable<IMessage>;
}

@Injectable({ providedIn: "root" })
export class StompClient implements IStompClient {

    private rxStomp: RxStomp | null = null;
    private config = inject(WEB_API_CONFIG);

    watch(opts: IWatchParams): Observable<IMessage> {
        if(this.rxStomp == null) {
            throw new Error("Stomp client is not initialized");
        }
        return this.rxStomp.watch(opts);
    }

    activate(): void {
        this.rxStomp = new RxStomp();

        this.rxStomp.configure({
            brokerURL: this.config.stompApiUrl()
        });

        this.rxStomp.activate();
    }

}