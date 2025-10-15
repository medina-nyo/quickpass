import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SignupEmailCode } from './signup-email-code';

describe('SignupEmailCode', () => {
  let component: SignupEmailCode;
  let fixture: ComponentFixture<SignupEmailCode>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SignupEmailCode]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SignupEmailCode);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
