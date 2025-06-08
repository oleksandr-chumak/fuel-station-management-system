import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FuelStationFuelPricesComponent } from './fuel-station-fuel-prices.component';

describe('FuelStationFuelPricesComponent', () => {
  let component: FuelStationFuelPricesComponent;
  let fixture: ComponentFixture<FuelStationFuelPricesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FuelStationFuelPricesComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(FuelStationFuelPricesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
