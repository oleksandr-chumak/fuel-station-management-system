export interface FuelPurchase {
    fuelPurchaseId: number;
    fuelOrderId: number;
    fuelStationId: number;
    fuelGrade: string;
    amount: number;
    pricePerLiter: number;
    currency: string;
    totalPrice: number;
    purchasedAt: string;
}
