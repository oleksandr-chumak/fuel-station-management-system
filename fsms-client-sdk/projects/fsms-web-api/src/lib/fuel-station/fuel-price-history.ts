export interface FuelStationFuelPriceHistoryEntry {
    fuelGrade: string;
    pricePerLiter: number;
    currency: string;
    changedAt: string;
    changedBy: number | null;
}
