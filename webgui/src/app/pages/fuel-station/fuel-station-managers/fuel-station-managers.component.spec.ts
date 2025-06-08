import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FuelStationManagersComponent } from './fuel-station-managers.component';

describe('FuelStationManagersComponent', () => {
  let component: FuelStationManagersComponent;
  let fixture: ComponentFixture<FuelStationManagersComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FuelStationManagersComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(FuelStationManagersComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
