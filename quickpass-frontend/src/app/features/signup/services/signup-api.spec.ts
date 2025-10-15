import { TestBed } from '@angular/core/testing';
import { SignupApiService } from './signup-api'; 
import { HttpClientTestingModule } from '@angular/common/http/testing'; 

describe('SignupApiService', () => { 
  let service: SignupApiService; 

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule]
    });
    service = TestBed.inject(SignupApiService);
  });

  it('✅ should be created', () => {
    expect(service).toBeTruthy();
  });
});