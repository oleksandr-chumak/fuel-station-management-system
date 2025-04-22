import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminFuelStationInfoComponent } from './admin-fuel-station-info.component';

describe('AdminFuelStationInfoComponent', () => {
  let component: AdminFuelStationInfoComponent;
  let fixture: ComponentFixture<AdminFuelStationInfoComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AdminFuelStationInfoComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AdminFuelStationInfoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
