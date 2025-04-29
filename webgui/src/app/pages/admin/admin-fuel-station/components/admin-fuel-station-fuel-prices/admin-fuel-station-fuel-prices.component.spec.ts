import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminFuelStationFuelPricesComponent } from './admin-fuel-station-fuel-prices.component';

describe('AdminFuelStationFuelPricesComponent', () => {
  let component: AdminFuelStationFuelPricesComponent;
  let fixture: ComponentFixture<AdminFuelStationFuelPricesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AdminFuelStationFuelPricesComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AdminFuelStationFuelPricesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
