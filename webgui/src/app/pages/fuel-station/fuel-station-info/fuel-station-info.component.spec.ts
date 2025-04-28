import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FuelStationInfoComponent } from './fuel-station-info.component';

describe('FuelStationInfoComponent', () => {
  let component: FuelStationInfoComponent;
  let fixture: ComponentFixture<FuelStationInfoComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FuelStationInfoComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(FuelStationInfoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
