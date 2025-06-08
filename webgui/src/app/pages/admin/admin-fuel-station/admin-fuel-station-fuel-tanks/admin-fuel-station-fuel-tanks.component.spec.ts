import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminFuelStationFuelTanksComponent } from './admin-fuel-station-fuel-tanks.component';

describe('AdminFuelStationFuelTanksComponent', () => {
  let component: AdminFuelStationFuelTanksComponent;
  let fixture: ComponentFixture<AdminFuelStationFuelTanksComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AdminFuelStationFuelTanksComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(AdminFuelStationFuelTanksComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
