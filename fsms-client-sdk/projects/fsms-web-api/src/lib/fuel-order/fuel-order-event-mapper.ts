import { Injectable } from "@angular/core";

import { FuelOrderConfirmed, FuelOrderCreated, FuelOrderProcessed, FuelOrderRejected } from "./fuel-order-events";
import { IMessage } from "@stomp/rx-stomp";

@Injectable({providedIn: "root" })
export class FuelOrderEventMapper {

    parseFuelOrderCreated(message: IMessage): FuelOrderCreated {
        const json = JSON.parse(message.body);
        return new FuelOrderCreated(json.fuelOrderId, json.fuelStationId);
    }

    parseFuelOrderConfirmed(message: IMessage): FuelOrderConfirmed {
        const json = JSON.parse(message.body);
        return new FuelOrderConfirmed(json.fuelOrderId, json.fuelStationId);
    }

    parseFuelOrderRejected(message: IMessage): FuelOrderRejected {
        const json = JSON.parse(message.body);
        return new FuelOrderRejected(json.fuelOrderId, json.fuelStationId);
    }

    parseFuelOrderProcessed(message: IMessage): FuelOrderProcessed {
        const json = JSON.parse(message.body);
        return new FuelOrderProcessed(json.fuelOrderId, json.fuelStationId);
    }

}