import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AddOrUpdateProjectComponent } from './add-or-update-project.component';

describe('AddOrUpdateProjectComponent', () => {
  let component: AddOrUpdateProjectComponent;
  let fixture: ComponentFixture<AddOrUpdateProjectComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AddOrUpdateProjectComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AddOrUpdateProjectComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
