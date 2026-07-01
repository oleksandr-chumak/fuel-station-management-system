import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { InputNumberModule } from 'primeng/inputnumber';
import { SelectModule } from 'primeng/select';
import { TranslatePipe } from '@ngx-translate/core';
import { FuelTank } from 'fsms-web-api';

export type AllocationFormGroup = FormGroup<{
  fuelTankId: FormControl<number | null>;
  volume: FormControl<number | null>;
}>;

export interface TankOption {
  id: number;
  tank: FuelTank;
}

interface BarSegments {
  inStock: number;
  pending: number;
  thisOrder: number;
}

@Component({
  selector: 'app-fuel-order-allocation-form',
  imports: [CommonModule, ReactiveFormsModule, ButtonModule, InputNumberModule, SelectModule, TranslatePipe],
  templateUrl: './fuel-order-allocation-form.html'
})
export class FuelOrderAllocationForm {
  @Input({ required: true }) form!: AllocationFormGroup;
  @Input({ required: true }) tankOptions: TankOption[] = [];
  @Input() selectedTank: FuelTank | null = null;
  @Input() canRemove: boolean = false;
  @Output() remove = new EventEmitter<void>();

  private get thisOrderVolume(): number {
    const v = this.form.controls.volume.value;
    return typeof v === 'number' && v > 0 ? v : 0;
  }

  private get freeBeforeOrder(): number {
    if (!this.selectedTank) return 0;
    return this.selectedTank.availableVolume;
  }

  private get freeAfterOrder(): number {
    return this.freeBeforeOrder - this.thisOrderVolume;
  }

  protected get inStockLabel(): string {
    return this.formatL(this.selectedTank?.currentVolume ?? 0);
  }

  protected get pendingLabel(): string {
    return this.formatL(this.selectedTank?.pendingVolume ?? 0);
  }

  protected get thisOrderLabel(): string {
    return this.formatL(this.thisOrderVolume);
  }

  protected get capacityLabel(): string {
    return this.formatL(this.selectedTank?.maxCapacity ?? 0);
  }

  protected get freeLabel(): string {
    return this.formatL(Math.max(0, this.freeAfterOrder));
  }

  protected get freeClass(): string {
    if (!this.selectedTank) return '';
    return this.colorClassFor(this.freeAfterOrder, this.selectedTank.maxCapacity);
  }

  protected get thisOrderColorClass(): string {
    return this.thisOrderVolume > this.freeBeforeOrder ? 'bg-red-500' : 'bg-emerald-500';
  }

  protected get barSegments(): BarSegments {
    if (!this.selectedTank) return { inStock: 0, pending: 0, thisOrder: 0 };
    const inStock = Math.max(0, this.selectedTank.currentVolume);
    const pending = Math.max(0, this.selectedTank.pendingVolume);
    const thisOrder = Math.max(0, this.thisOrderVolume);
    const denom = Math.max(this.selectedTank.maxCapacity, inStock + pending + thisOrder);
    if (denom === 0) return { inStock: 0, pending: 0, thisOrder: 0 };
    return {
      inStock: (inStock / denom) * 100,
      pending: (pending / denom) * 100,
      thisOrder: (thisOrder / denom) * 100
    };
  }

  protected get overCapacityMessage(): { free: string; tankId: number } | null {
    const ctl = this.form.controls.volume;
    if (!ctl.touched) return null;
    const err = ctl.errors?.['overCapacity'];
    if (!err) return null;
    return { free: this.formatL(err.free), tankId: err.tankId };
  }

  protected get showTankRequiredMessage(): boolean {
    const ctl = this.form.controls.fuelTankId;
    return !!(ctl.touched && ctl.hasError('required'));
  }

  protected get showVolumeRequiredMessage(): boolean {
    const ctl = this.form.controls.volume;
    return !!(ctl.touched && (ctl.hasError('required') || ctl.hasError('min')));
  }

  protected optionInStockLabel(tank: FuelTank): string {
    return this.formatL(tank.currentVolume);
  }

  protected optionPendingLabel(tank: FuelTank): string {
    return this.formatL(tank.pendingVolume);
  }

  protected optionFreeLabel(tank: FuelTank): string {
    return this.formatL(Math.max(0, tank.availableVolume));
  }

  protected optionFreeClass(tank: FuelTank): string {
    return this.colorClassFor(tank.availableVolume, tank.maxCapacity);
  }

  private colorClassFor(free: number, capacity: number): string {
    if (free <= 0) return 'text-red-500';
    if (capacity > 0 && free < capacity * 0.25) return 'text-amber-500';
    return 'text-emerald-500';
  }

  private formatL(v: number): string {
    return `${Math.round(v).toLocaleString('en-US')} L`;
  }
}
