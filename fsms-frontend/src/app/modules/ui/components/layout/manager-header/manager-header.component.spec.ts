import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ManagerHeaderComponent } from './manager-header.component';

describe('ManagerHeaderComponent', () => {
  let component: ManagerHeaderComponent;
  let fixture: ComponentFixture<ManagerHeaderComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ManagerHeaderComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ManagerHeaderComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
