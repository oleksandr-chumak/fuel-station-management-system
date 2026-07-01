import { Component, computed, DestroyRef, EventEmitter, inject, Input, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AbstractControl, FormArray, FormControl, FormGroup, ReactiveFormsModule, ValidatorFn, Validators } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { MessageModule } from 'primeng/message';
import { SelectModule } from 'primeng/select';
import { toSignal, takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { TranslatePipe } from '@ngx-translate/core';
import { FuelGrade, FuelOrderAllocation, FuelStation, FuelTank } from 'fsms-web-api';
import { BehaviorSubject, startWith } from 'rxjs';
import { AllocationFormGroup, FuelOrderAllocationForm, TankOption } from './fuel-order-allocation-form';
import { LanguageService } from '../../../common/language.service';

export interface CreateFuelOrderFormValue {
  fuelGrade: FuelGrade;
  allocations: FuelOrderAllocation[];
}

@Component({
  selector: 'app-create-fuel-order-form',
  imports: [CommonModule, ReactiveFormsModule, ButtonModule, MessageModule, SelectModule, TranslatePipe, FuelOrderAllocationForm],
  templateUrl: './create-fuel-order-form.html'
})
export class CreateFuelOrderForm {
  private readonly destroyRef = inject(DestroyRef);
  private readonly language = inject(LanguageService);

  private readonly fuelStation$ = new BehaviorSubject<FuelStation | null>(null);
  private readonly loading$ = new BehaviorSubject<boolean>(false);

  @Input() set fuelStation(value: FuelStation | null) {
    this.fuelStation$.next(value);
  }

  @Input() set loading(value: boolean) {
    this.loading$.next(value);
  }

  @Output() formSubmit = new EventEmitter<CreateFuelOrderFormValue>();

  protected readonly loadingSignal = toSignal(this.loading$, { initialValue: false });
  private readonly fuelStationSignal = toSignal(this.fuelStation$, { initialValue: null });

  protected readonly createFuelOrderForm = new FormGroup({
    fuelGrade: new FormControl<FuelGrade | null>(null, Validators.required),
    allocations: new FormArray<AllocationFormGroup>([], Validators.required)
  });

  private readonly formValue = toSignal(
    this.createFuelOrderForm.valueChanges.pipe(startWith(this.createFuelOrderForm.getRawValue())),
    { initialValue: this.createFuelOrderForm.getRawValue() }
  );

  protected readonly selectedGrade = computed(() => this.formValue().fuelGrade ?? null);

  protected readonly fuelGrades = computed(() => {
    const tanks = this.fuelStationSignal()?.fuelTanks ?? [];
    const counts = new Map<FuelGrade, number>();
    for (const t of tanks) {
      counts.set(t.fuelGrade, (counts.get(t.fuelGrade) ?? 0) + 1);
    }
    return Array.from(counts.entries()).map(([grade, count]) => ({
      labelKey: this.gradeLabelKey(grade),
      value: grade,
      tankCount: count
    }));
  });

  protected readonly hasFuelGrades = computed(() => this.fuelGrades().length > 0);

  protected readonly tanksOfGrade = computed<FuelTank[]>(() => {
    const grade = this.selectedGrade();
    if (grade === null) return [];
    return (this.fuelStationSignal()?.fuelTanks ?? []).filter(t => t.fuelGrade === grade);
  });

  protected readonly canAddAllocation = computed(() => {
    if (this.selectedGrade() === null) return false;
    return this.allocations.length < this.tanksOfGrade().length;
  });

  protected readonly tankOptionsByRow = computed<Map<number, TankOption[]>>(() => {
    const value = this.formValue();
    const tanks = this.tanksOfGrade();
    const rowSelections = (value.allocations ?? []).map(r => r?.fuelTankId ?? null);
    const map = new Map<number, TankOption[]>();
    for (let i = 0; i < rowSelections.length; i++) {
      const takenByOthers = new Set(
        rowSelections
          .filter((_, j) => j !== i)
          .filter((id): id is number => id !== null && id !== undefined)
      );
      map.set(
        i,
        tanks
          .filter(t => !takenByOthers.has(t.id))
          .map(t => ({ id: t.id, tank: t }))
      );
    }
    return map;
  });

  protected readonly selectedTankByRow = computed<Map<number, FuelTank | null>>(() => {
    const value = this.formValue();
    const rowSelections = (value.allocations ?? []).map(r => r?.fuelTankId ?? null);
    const tanks = this.fuelStationSignal()?.fuelTanks ?? [];
    const map = new Map<number, FuelTank | null>();
    rowSelections.forEach((id, i) => {
      map.set(i, id != null ? tanks.find(t => t.id === id) ?? null : null);
    });
    return map;
  });

  protected readonly totalVolume = computed(() => {
    const value = this.formValue();
    return (value.allocations ?? [])
      .map(r => r?.volume)
      .filter((v): v is number => typeof v === 'number' && v > 0)
      .reduce((a, b) => a + b, 0);
  });

  protected readonly totalVolumeLabel = computed(() => this.formatL(this.totalVolume()));

  protected readonly blockingReasonKey = computed<string | null>(() => {
    const value = this.formValue();
    if (!this.hasFuelGrades()) return 'fuelOrders.helper.noTanks';
    if (value.fuelGrade == null) return 'fuelOrders.helper.noGrade';
    const rows = value.allocations ?? [];
    if (rows.length === 0) return 'fuelOrders.helper.noAllocation';
    for (let i = 0; i < this.allocations.length; i++) {
      const row = this.allocations.at(i);
      const v = row.value;
      if (v.fuelTankId == null) return 'fuelOrders.helper.missingTank';
      if (v.volume == null || v.volume <= 0) return 'fuelOrders.helper.missingVolume';
      if (row.controls.volume.hasError('overCapacity')) return 'fuelOrders.helper.overCapacity';
    }
    return null;
  });

  protected readonly submitDisabled = computed(() =>
    this.blockingReasonKey() !== null || this.loadingSignal()
  );

  constructor() {
    this.createFuelOrderForm.controls.fuelGrade.valueChanges
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe(() => this.resetAllocations());
  }

  get allocations(): FormArray<AllocationFormGroup> {
    return this.createFuelOrderForm.controls.allocations;
  }

  protected addAllocation(): void {
    this.allocations.push(this.buildAllocationGroup());
  }

  protected removeAllocation(index: number): void {
    if (this.allocations.length > 1) {
      this.allocations.removeAt(index);
    }
  }

  get fuelGradeInvalid(): boolean {
    return this.isFieldInvalid(this.createFuelOrderForm, 'fuelGrade');
  }

  handleFormSubmission(): void {
    if (this.createFuelOrderForm.valid && this.allocations.length > 0) {
      const allocations = this.allocations.controls.map(group => new FuelOrderAllocation(
        group.value.fuelTankId!,
        group.value.volume!
      ));
      this.formSubmit.emit({
        fuelGrade: this.createFuelOrderForm.value.fuelGrade!,
        allocations
      });
      return;
    }
    this.createFuelOrderForm.markAllAsTouched();
  }

  private resetAllocations(): void {
    this.allocations.clear();
    if (this.selectedGrade() !== null) {
      this.addAllocation();
    }
  }

  private buildAllocationGroup(): AllocationFormGroup {
    const group: AllocationFormGroup = new FormGroup({
      fuelTankId: new FormControl<number | null>(null, Validators.required),
      volume: new FormControl<number | null>(null, [Validators.required, Validators.min(0.001)])
    });
    group.controls.volume.addValidators(
      this.overCapacityValidator(() => this.lookupTank(group.controls.fuelTankId.value))
    );
    group.controls.fuelTankId.valueChanges
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe(() => group.controls.volume.updateValueAndValidity({ emitEvent: false }));
    return group;
  }

  private overCapacityValidator(getTank: () => FuelTank | null): ValidatorFn {
    return (control: AbstractControl) => {
      const volume = control.value;
      if (typeof volume !== 'number' || volume <= 0) return null;
      const tank = getTank();
      if (!tank) return null;
      const free = tank.availableVolume;
      if (volume > free) {
        return { overCapacity: { free, tankId: tank.id } };
      }
      return null;
    };
  }

  private lookupTank(id: number | null): FuelTank | null {
    if (id == null) return null;
    return this.fuelStationSignal()?.fuelTanks?.find(t => t.id === id) ?? null;
  }

  private formatL(v: number): string {
    return `${Math.round(v).toLocaleString('en-US')} L`;
  }

  private isFieldInvalid(formGroup: FormGroup, fieldName: string): boolean {
    return !!(formGroup.get(fieldName)?.touched && formGroup.get(fieldName)?.invalid);
  }

  protected tanksCountKey(count: number): string {
    if (this.language.current() === 'uk') {
      const mod10 = count % 10;
      const mod100 = count % 100;
      if (mod10 === 1 && mod100 !== 11) return 'fuelOrders.tanksCountOne';
      if (mod10 >= 2 && mod10 <= 4 && (mod100 < 12 || mod100 > 14)) return 'fuelOrders.tanksCountFew';
      return 'fuelOrders.tanksCountMany';
    }
    return count === 1 ? 'fuelOrders.tanksCountOne' : 'fuelOrders.tanksCountMany';
  }

  private gradeLabelKey(grade: FuelGrade): string {
    const name = FuelGrade[grade];
    switch (name.toLowerCase().replace(/[-_\s]/g, '')) {
      case 'ron92': return 'fuelGrades.ron92';
      case 'ron95': return 'fuelGrades.ron95';
      case 'diesel': return 'fuelGrades.diesel';
      default: return 'fuelGrades.unknown';
    }
  }
}
