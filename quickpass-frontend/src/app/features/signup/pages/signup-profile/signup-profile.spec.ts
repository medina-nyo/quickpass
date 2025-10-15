import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SignupProfile } from './signup-profile';

describe('SignupProfile', () => {
  let component: SignupProfile;
  let fixture: ComponentFixture<SignupProfile>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SignupProfile]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SignupProfile);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
