export interface TaxRule {
    taxRuleId: number;
    countryCode: string;
    fuelGrade: string;
    taxType: string;
    nameLocal: string;
    nameEnglish: string;
    description: string;
    valueType: string;
    amount: number;
    currency: string | null;
    unit: string | null;
    effectiveFrom: string;
    effectiveTo: string | null;
}
