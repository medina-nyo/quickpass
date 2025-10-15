import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';

/**
 * Service responsable des appels API liés au parcours d'inscription.
 */
@Injectable({ providedIn: 'root' })
export class SignupApiService {
  private readonly baseUrl = `${environment.apiBaseUrl}/signup`;

  constructor(private http: HttpClient) {}

  /**
   * Démarre l'inscription à partir d'une adresse e-mail.
   * @param email Adresse e-mail du boulanger.
   */
  startSignup(email: string): Observable<any> {
    return this.http.post(`${this.baseUrl}/start`, { email });
  }
}
