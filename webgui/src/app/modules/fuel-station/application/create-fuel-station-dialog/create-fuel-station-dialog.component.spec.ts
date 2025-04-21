import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreateFuelStationDialogComponent } from './create-fuel-station-dialog.component';

describe('CreateFuelStationDialogComponent', () => {
  let component: CreateFuelStationDialogComponent;
  let fixture: ComponentFixture<CreateFuelStationDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CreateFuelStationDialogComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CreateFuelStationDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
