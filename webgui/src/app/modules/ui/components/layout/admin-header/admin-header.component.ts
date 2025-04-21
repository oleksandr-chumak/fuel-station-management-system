import { Component } from '@angular/core';
import { MenuItem } from 'primeng/api';
import { MenubarModule } from 'primeng/menubar';

@Component({
  selector: 'app-admin-header',
  standalone: true,
  imports: [MenubarModule],
  templateUrl: './admin-header.component.html',
})
export class AdminHeaderComponent {

  get items(): MenuItem[] {
    return []
  }

}
