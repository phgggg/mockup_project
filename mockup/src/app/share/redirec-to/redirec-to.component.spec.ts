import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RedirecToComponent } from './redirec-to.component';

describe('RedirecToComponent', () => {
  let component: RedirecToComponent;
  let fixture: ComponentFixture<RedirecToComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RedirecToComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RedirecToComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
