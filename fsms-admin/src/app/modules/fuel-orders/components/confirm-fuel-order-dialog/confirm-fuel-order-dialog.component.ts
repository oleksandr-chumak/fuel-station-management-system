import { CommonModule } from '@angular/common';
import { Component, inject, output, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { DialogModule } from 'primeng/dialog';
import { InputNumberModule } from 'primeng/inputnumber';
import { MessageModule } from 'primeng/message';
import { TranslatePipe } from '@ngx-translate/core';
import { FuelOrder, FuelOrderRecommendedPrice, FuelOrderRestClient } from 'fsms-web-api';
import { catchError, EMPTY, tap } from 'rxjs';
import BasicDialog from '../../../common/basic-dialog.component';

@Component({
  selector: 'app-confirm-fuel-order-dialog',
  imports: [CommonModule, FormsModule, DialogModule, ButtonModule, InputNumberModule, MessageModule, TranslatePipe],
  templateUrl: './confirm-fuel-order-dialog.component.html'
})
export class ConfirmFuelOrderDialogComponent extends BasicDialog {

  private readonly api = inject(FuelOrderRestClient);

  confirmed = output<{ fuelOrderId: number; pricePerLiter: number }>();

  protected readonly fuelOrder = signal<FuelOrder | null>(null);
  protected readonly recommendation = signal<FuelOrderRecommendedPrice | null>(null);
  protected readonly loadingRecommendation = signal(false);
  protected readonly recommendationFailed = signal(false);
  protected pricePerLiter: number | null = null;

  openFor(fuelOrder: FuelOrder): void {
    this.fuelOrder.set(fuelOrder);
    this.recommendation.set(null);
    this.recommendationFailed.set(false);
    this.pricePerLiter = null;
    this.loadingRecommendation.set(true);
    this.api.getRecommendedPrice(fuelOrder.fuelOrderId)
      .pipe(
        tap((res) => {
          this.recommendation.set(res);
          if (this.pricePerLiter === null) {
            this.pricePerLiter = res.pricePerLiter;
          }
          this.loadingRecommendation.set(false);
        }),
        catchError(() => {
          this.recommendationFailed.set(true);
          this.loadingRecommendation.set(false);
          return EMPTY;
        })
      )
      .subscribe();
    super.openDialog();
  }

  get canSubmit(): boolean {
    return this.fuelOrder() !== null
      && this.pricePerLiter !== null
      && this.pricePerLiter > 0;
  }

  useRecommendation(): void {
    const rec = this.recommendation();
    if (rec) {
      this.pricePerLiter = rec.pricePerLiter;
    }
  }

  submit(): void {
    const order = this.fuelOrder();
    if (!this.canSubmit || order === null) {
      return;
    }
    this.confirmed.emit({ fuelOrderId: order.fuelOrderId, pricePerLiter: this.pricePerLiter! });
    this.closeDialog();
  }

}
