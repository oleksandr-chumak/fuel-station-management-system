import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminFuelStationComponent } from './admin-fuel-station.component';

describe('AdminFuelStationComponent', () => {
  let component: AdminFuelStationComponent;
  let fixture: ComponentFixture<AdminFuelStationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AdminFuelStationComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AdminFuelStationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
