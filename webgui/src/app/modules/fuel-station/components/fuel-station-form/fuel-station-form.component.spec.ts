import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FuelStationFormComponent } from './fuel-station-form.component';

describe('FuelStationFormComponent', () => {
  let component: FuelStationFormComponent;
  let fixture: ComponentFixture<FuelStationFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FuelStationFormComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(FuelStationFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
