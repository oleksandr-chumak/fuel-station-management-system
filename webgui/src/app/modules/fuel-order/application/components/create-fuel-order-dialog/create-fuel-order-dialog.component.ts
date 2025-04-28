import { Component } from '@angular/core';
import BasicDialog from '../../../../common/application/basic-dialog.component';
import { DialogModule } from 'primeng/dialog';

@Component({
  selector: 'app-create-fuel-order-dialog',
  imports: [DialogModule],
  templateUrl: './create-fuel-order-dialog.component.html'
})
export class CreateFuelOrderDialogComponent extends BasicDialog {
    
}
