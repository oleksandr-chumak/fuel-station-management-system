import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminFuelStationFuelOrdersComponent } from './admin-fuel-station-fuel-orders.component';

describe('AdminFuelStationFuelOrdersComponent', () => {
  let component: AdminFuelStationFuelOrdersComponent;
  let fixture: ComponentFixture<AdminFuelStationFuelOrdersComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AdminFuelStationFuelOrdersComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(AdminFuelStationFuelOrdersComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
