import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreateFuelOrderDialogComponent } from './create-fuel-order-dialog.component';

describe('CreateFuelOrderDialogComponent', () => {
  let component: CreateFuelOrderDialogComponent;
  let fixture: ComponentFixture<CreateFuelOrderDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CreateFuelOrderDialogComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(CreateFuelOrderDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
