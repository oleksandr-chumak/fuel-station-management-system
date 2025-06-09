import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FuelStationsAdminComponent } from './fuel-stations-admin.component';

describe('FuelStationsAdminComponent', () => {
  let component: FuelStationsAdminComponent;
  let fixture: ComponentFixture<FuelStationsAdminComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FuelStationsAdminComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(FuelStationsAdminComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
