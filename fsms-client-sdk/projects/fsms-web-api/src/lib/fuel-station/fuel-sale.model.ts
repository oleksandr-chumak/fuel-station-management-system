export interface FuelSale {
    fuelSaleId: number;
    fuelStationId: number;
    fuelTankId: number;
    fuelGrade: string;
    volume: number;
    pricePerLiter: number;
    currency: string;
    totalRevenue: number;
    soldAt: string;
}
