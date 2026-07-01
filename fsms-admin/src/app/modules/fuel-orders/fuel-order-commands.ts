export interface GetFuelOrders {
}

export interface ConfirmFuelOrder {
    fuelOrderId: number;
    pricePerLiter: number;
}

export interface RejectFuelOrder {
    fuelOrderId: number;
}
