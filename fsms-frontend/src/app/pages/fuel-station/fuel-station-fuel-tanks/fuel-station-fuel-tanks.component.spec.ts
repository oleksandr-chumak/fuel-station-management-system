import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FuelStationFuelTanksComponent } from './fuel-station-fuel-tanks.component';

describe('FuelStationFuelTanksComponent', () => {
  let component: FuelStationFuelTanksComponent;
  let fixture: ComponentFixture<FuelStationFuelTanksComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FuelStationFuelTanksComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(FuelStationFuelTanksComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
