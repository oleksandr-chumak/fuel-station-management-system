import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminFuelOrdersComponent } from './admin-fuel-orders.component';

describe('AdminFuelOrdersComponent', () => {
  let component: AdminFuelOrdersComponent;
  let fixture: ComponentFixture<AdminFuelOrdersComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AdminFuelOrdersComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AdminFuelOrdersComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
