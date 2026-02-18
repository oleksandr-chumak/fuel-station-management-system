import { Component, DestroyRef, inject, OnInit } from '@angular/core';
import { ButtonModule } from 'primeng/button';
import { PanelModule } from 'primeng/panel';
import { CreateManagerDialogComponent } from '../../modules/managers/components/create-manager-dialog/create-manager-dialog.component';
import { ManagerTable } from '../../modules/managers/components/manager-table/manager-table';
import { ManagerTemplateDirective } from '../../modules/managers/directives/manager-template-directive';
import { SignInAsManagerButton } from '../../modules/managers/components/sign-in-as-manager-button/sign-in-as-manager-button';
import { Manager } from 'fsms-web-api';
import { GetManagersHandler } from '../../modules/managers/handlers/get-managers-handler';
import { takeUntilDestroyed, toSignal } from '@angular/core/rxjs-interop';
import { tap } from 'rxjs';

@Component({
  selector: 'app-managers-page',
  imports: [PanelModule, ButtonModule, CreateManagerDialogComponent, ManagerTable, ManagerTemplateDirective, SignInAsManagerButton],
  templateUrl: './managers.page.html'
})
export class ManagersPage implements OnInit {
  private readonly destroyRef = inject(DestroyRef);
  private readonly getManagersHandler = inject(GetManagersHandler);

  protected managers: Manager[] = [];
  protected readonly loading = toSignal(this.getManagersHandler.loading$, { initialValue: false });

  ngOnInit(): void {
    this.fetchManagers();
  }

  protected fetchManagers() {
    this.getManagersHandler
      .handle({})
      .pipe(
        tap(managers => this.managers = managers),
        takeUntilDestroyed(this.destroyRef)
      )
      .subscribe();
  }
}
