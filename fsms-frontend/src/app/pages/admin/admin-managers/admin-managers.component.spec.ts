import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminManagersComponent } from './admin-managers.component';

describe('AdminManagersComponent', () => {
  let component: AdminManagersComponent;
  let fixture: ComponentFixture<AdminManagersComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AdminManagersComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AdminManagersComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
