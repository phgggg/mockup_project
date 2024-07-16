import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { WebHomePageComponent } from './web-home-page.component';

describe('WebHomePageComponent', () => {
  let component: WebHomePageComponent;
  let fixture: ComponentFixture<WebHomePageComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ WebHomePageComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(WebHomePageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
