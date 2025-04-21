import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FuelStationAdminComponent } from './fuel-station-admin.component';

describe('FuelStationAdminComponent', () => {
  let component: FuelStationAdminComponent;
  let fixture: ComponentFixture<FuelStationAdminComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FuelStationAdminComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(FuelStationAdminComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
