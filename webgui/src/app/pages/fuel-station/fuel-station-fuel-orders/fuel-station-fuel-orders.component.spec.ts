import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FuelStationFuelOrdersComponent } from './fuel-station-fuel-orders.component';

describe('FuelStationFuelOrdersComponent', () => {
  let component: FuelStationFuelOrdersComponent;
  let fixture: ComponentFixture<FuelStationFuelOrdersComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FuelStationFuelOrdersComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(FuelStationFuelOrdersComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
