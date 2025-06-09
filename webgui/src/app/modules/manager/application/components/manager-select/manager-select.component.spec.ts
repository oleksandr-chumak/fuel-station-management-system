import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ManagerSelectComponent } from './manager-select.component';

describe('ManagerSelectComponent', () => {
  let component: ManagerSelectComponent;
  let fixture: ComponentFixture<ManagerSelectComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ManagerSelectComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(ManagerSelectComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
