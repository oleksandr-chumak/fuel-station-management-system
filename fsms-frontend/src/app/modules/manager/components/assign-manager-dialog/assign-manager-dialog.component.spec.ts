import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AssignManagerDialogComponent } from './assign-manager-dialog.component';

describe('AssignManagerDialogComponent', () => {
  let component: AssignManagerDialogComponent;
  let fixture: ComponentFixture<AssignManagerDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AssignManagerDialogComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AssignManagerDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
