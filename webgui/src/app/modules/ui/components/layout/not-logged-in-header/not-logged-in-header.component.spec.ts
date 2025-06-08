import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NotLoggedInHeaderComponent } from './not-logged-in-header.component';

describe('NotLoggedInHeaderComponent', () => {
  let component: NotLoggedInHeaderComponent;
  let fixture: ComponentFixture<NotLoggedInHeaderComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [NotLoggedInHeaderComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(NotLoggedInHeaderComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
