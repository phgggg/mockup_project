import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PeojectDetailComponent } from './peoject-detail.component';

describe('PeojectDetailComponent', () => {
  let component: PeojectDetailComponent;
  let fixture: ComponentFixture<PeojectDetailComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PeojectDetailComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PeojectDetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
