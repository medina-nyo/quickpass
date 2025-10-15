import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { SignupApiService } from '../../services/signup-api';
import { CommonModule } from '@angular/common';
import { finalize, tap } from 'rxjs'; 

const EMAIL_REGEX = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[A-Za-z]{2,63}$/; 

/**
 * Composant Step 1 du parcours d’inscription.
 * Permet à l’utilisateur de saisir et valider son adresse e-mail.
 */
@Component({
  selector: 'app-signup-email',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './signup-email.html',
  styleUrls: ['./signup-email.scss']
})
export class SignupEmailComponent {
  /** Formulaire réactif d’inscription */
  form: FormGroup;

  /** Indique si une requête HTTP est en cours */
  loading = false;

  constructor(
    private readonly fb: FormBuilder,
    private readonly router: Router,
    private readonly toastr: ToastrService,
    private readonly signupApi: SignupApiService
  ) {
    this.form = this.fb.group({
      email: [
        '',
        [
          Validators.required, 
          Validators.pattern(EMAIL_REGEX), 
          Validators.maxLength(255)
        ]
      ]
    });
  }

  /** Getter pratique pour accéder au champ email depuis le template */
  get email() {
    return this.form.get('email');
  }

  /** Message d’erreur dynamique du champ email. */
  get emailErrorMessage(): string | null {
    const ctrl = this.email;
    if (ctrl?.hasError('required')) return "L’adresse e-mail est obligatoire.";
    if (ctrl?.hasError('pattern')) return 'Format d’adresse e-mail invalide.'; 
    return null;
  }

  /** Soumet le formulaire. */
  onSubmit(): void {
    if (this.loading) return; 

    const rawEmail = this.email?.value?.trim();
    this.email?.setValue(rawEmail);

    if (this.form.invalid) {
      this.toastr.error('Veuillez entrer une adresse e-mail valide.');
      this.form.markAllAsTouched();
      return;
    }
    
    this.email?.disable(); 

    this.signupApi.startSignup(rawEmail).pipe(
      tap(() => this.loading = true), 
      finalize(() => {
        this.loading = false; 
        this.email?.enable(); 
      })
    ).subscribe({
      next: (response) => {
        if (response?.success) {
            this.toastr.success('Un code de vérification vous a été envoyé. Veuillez consulter votre boîte mail.');          
            this.router.navigate(['/signup/code'], {
                queryParams: { sessionId: response.data?.sessionId }
            });
        } else {
          this.toastr.error('Une erreur est survenue. Veuillez réessayer.');
        }
      },
      error: (err) => {
        console.error('Erreur backend:', err);
        this.toastr.error('Erreur lors de l’envoi de l’e-mail.');
      }
    });
  }
}