import { Component, computed, DestroyRef, inject } from '@angular/core';
import { DialogModule } from 'primeng/dialog';
import { CommonModule } from '@angular/common';
import { FormArray, FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { MessageModule } from 'primeng/message';
import { SelectModule } from 'primeng/select';
import { InputNumberModule } from 'primeng/inputnumber';
import { toSignal, takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { TranslatePipe } from '@ngx-translate/core';
import BasicDialog from '../../../common/basic-dialog.component';
import { FuelGrade, FuelOrderAllocation, FuelTank } from 'fsms-web-api';
import { CreateFuelOrderHandler } from '../../../fuel-station/handlers/create-fuel-order.handler';
import { FuelStationStore } from '../../../fuel-station/fuel-station-store';
import { startWith, tap } from 'rxjs';

type AllocationFormGroup = FormGroup<{
  fuelTankId: FormControl<number | null>;
  volume: FormControl<number | null>;
}>;

@Component({
  selector: 'app-create-fuel-order-dialog',
  imports: [CommonModule, ReactiveFormsModule, DialogModule, ButtonModule, MessageModule, InputTextModule, SelectModule, InputNumberModule, TranslatePipe],
  templateUrl: './create-fuel-order-dialog.component.html'
})
export class CreateFuelOrderDialogComponent extends BasicDialog {
  private readonly handler = inject(CreateFuelOrderHandler);
  private readonly store = inject(FuelStationStore);
  private readonly destroyRef = inject(DestroyRef);

  protected readonly loading = toSignal(this.handler.loading$, { initialValue: false });

  private readonly fuelStation = toSignal(this.store.fuelStation$, { initialValue: null });

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
    const tanks = this.fuelStation()?.fuelTanks ?? [];
    const uniqueGrades = Array.from(new Set(tanks.map(t => t.fuelGrade)));
    return uniqueGrades.map(grade => ({
      labelKey: this.gradeLabelKey(grade),
      value: grade
    }));
  });

  protected readonly hasFuelGrades = computed(() => this.fuelGrades().length > 0);

  protected readonly tanksOfGrade = computed<FuelTank[]>(() => {
    const grade = this.selectedGrade();
    if (grade === null) return [];
    return (this.fuelStation()?.fuelTanks ?? []).filter(t => t.fuelGrade === grade);
  });

  protected readonly canAddAllocation = computed(() => {
    if (this.selectedGrade() === null) return false;
    return this.allocations.length < this.tanksOfGrade().length;
  });

  constructor() {
    super();
    this.createFuelOrderForm.controls.fuelGrade.valueChanges
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe(() => this.resetAllocations());
  }

  get allocations(): FormArray<AllocationFormGroup> {
    return this.createFuelOrderForm.controls.allocations;
  }

  override openDialog(): void {
    this.createFuelOrderForm.reset({ fuelGrade: null });
    this.allocations.clear();
    super.openDialog();
  }

  protected addAllocation(): void {
    this.allocations.push(this.buildAllocationGroup());
  }

  protected removeAllocation(index: number): void {
    if (this.allocations.length > 1) {
      this.allocations.removeAt(index);
    }
  }

  protected tankOptionsFor(rowIndex: number): { label: string; value: number }[] {
    const tanks = this.tanksOfGrade();
    const selectedInOtherRows = this.allocations.controls
      .map((row, i) => i === rowIndex ? null : row.value.fuelTankId)
      .filter((id): id is number => id !== null && id !== undefined);
    return tanks
      .filter(t => !selectedInOtherRows.includes(t.id))
      .map(t => ({
        label: this.tankLabel(t),
        value: t.id
      }));
  }

  protected isAllocationFieldInvalid(rowIndex: number, fieldName: 'fuelTankId' | 'volume'): boolean {
    const control = this.allocations.at(rowIndex).get(fieldName);
    return !!(control?.touched && control?.invalid);
  }

  get fuelGradeInvalid(): boolean {
    return this.isFieldInvalid(this.createFuelOrderForm, "fuelGrade");
  }

  handleFormSubmission(): void {
    if (this.createFuelOrderForm.valid && this.allocations.length > 0) {
      const allocations = this.allocations.controls.map(group => new FuelOrderAllocation(
        group.value.fuelTankId!,
        group.value.volume!
      ));
      this.handler
        .handle({
          fuelStationId: this.store.fuelStation.fuelStationId,
          fuelGrade: this.createFuelOrderForm.value.fuelGrade!,
          allocations
        })
        .pipe(tap(() => this.closeDialog()))
        .subscribe();
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
    return new FormGroup({
      fuelTankId: new FormControl<number | null>(null, Validators.required),
      volume: new FormControl<number | null>(null, [Validators.required, Validators.min(0.001)])
    });
  }

  private tankLabel(tank: FuelTank): string {
    const free = tank.availableVolume;
    return tank.pendingVolume > 0
      ? `#${tank.id} (${free}L free / ${tank.maxCapacity}L, ${tank.pendingVolume}L pending)`
      : `#${tank.id} (${free}L free / ${tank.maxCapacity}L)`;
  }

  private isFieldInvalid(formGroup: FormGroup, fieldName: string): boolean {
    return !!(formGroup.get(fieldName)?.touched && formGroup.get(fieldName)?.invalid);
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
