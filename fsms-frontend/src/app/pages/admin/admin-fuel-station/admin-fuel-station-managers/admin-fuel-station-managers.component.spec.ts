import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminFuelStationManagersComponent } from './admin-fuel-station-managers.component';

describe('AdminFuelStationManagersComponent', () => {
  let component: AdminFuelStationManagersComponent;
  let fixture: ComponentFixture<AdminFuelStationManagersComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AdminFuelStationManagersComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AdminFuelStationManagersComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
