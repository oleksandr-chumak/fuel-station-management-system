import { Component, computed, input } from "@angular/core";
import { FuelGrade } from "fsms-web-api";
import { TranslatePipe } from "@ngx-translate/core";

@Component({
    selector: 'app-fuel-grade-label',
    standalone: true,
    imports: [TranslatePipe],
    template: `<span>{{ key() | translate }}</span>`,
})
export class FuelGradeLabel {
    grade = input.required<FuelGrade | string | number | null | undefined>();

    protected readonly key = computed(() => {
        const g = this.grade();
        if (g === null || g === undefined) return 'fuelGrades.unknown';
        const normalized = typeof g === 'number' ? FuelGrade[g] : String(g);
        switch (normalized.toLowerCase().replace(/[-_\s]/g, '')) {
            case 'ron92': return 'fuelGrades.ron92';
            case 'ron95': return 'fuelGrades.ron95';
            case 'diesel': return 'fuelGrades.diesel';
            default: return normalized;
        }
    });
}
