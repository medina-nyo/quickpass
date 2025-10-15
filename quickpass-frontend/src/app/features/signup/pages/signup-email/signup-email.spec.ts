import { TestBed, ComponentFixture } from '@angular/core/testing';
import { SignupEmailComponent } from './signup-email';
import { ReactiveFormsModule } from '@angular/forms';
import { ToastrService } from 'ngx-toastr';
import { Router } from '@angular/router';
import { SignupApiService } from '../../services/signup-api';
import { of, throwError } from 'rxjs';

/**
 * Suite de tests unitaires pour le composant SignupEmailComponent.
 */
describe('SignupEmailComponent', () => {
  let component: SignupEmailComponent;
  let fixture: ComponentFixture<SignupEmailComponent>;
  let toastr: jasmine.SpyObj<ToastrService>;
  let router: jasmine.SpyObj<Router>;
  let signupApi: jasmine.SpyObj<SignupApiService>;

  beforeEach(() => {
    toastr = jasmine.createSpyObj('ToastrService', ['success', 'error']);
    router = jasmine.createSpyObj('Router', ['navigate']);
    signupApi = jasmine.createSpyObj('SignupApiService', ['startSignup']);

    TestBed.configureTestingModule({
      imports: [ReactiveFormsModule, SignupEmailComponent], 
      providers: [
        { provide: ToastrService, useValue: toastr },
        { provide: Router, useValue: router },
        { provide: SignupApiService, useValue: signupApi }
      ]
    });

    fixture = TestBed.createComponent(SignupEmailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges(); 
  });

  /**
   * Teste la création du composant.
   */
  it('devrait créer le composant', () => {
    expect(component).toBeTruthy();
  });

  /**
   * Teste l'affichage de l'erreur si le champ email est vide.
   */
  it('devrait afficher une erreur si email vide', () => {
    component.form.setValue({ email: '' });
    component.form.updateValueAndValidity(); 
    component.onSubmit();
    
    expect(component.form.invalid).toBeTrue();
    expect(toastr.error).toHaveBeenCalledWith('Veuillez entrer une adresse e-mail valide.');
    expect(signupApi.startSignup).not.toHaveBeenCalled();
  });

  /**
   * Teste l'affichage de l'erreur si le format de l'email est invalide.
   */
  it('devrait afficher une erreur si email invalide', () => {
    component.form.setValue({ email: 'contact@' });
    component.form.updateValueAndValidity();
    component.onSubmit();
    
    expect(component.form.invalid).toBeTrue();
    expect(toastr.error).toHaveBeenCalledWith('Veuillez entrer une adresse e-mail valide.');
    expect(signupApi.startSignup).not.toHaveBeenCalled();
  });
  
  /**
   * Teste l'appel à l'API et la redirection en cas de succès.
   */
  it('devrait appeler API et rediriger si succès', () => {
    const mockSessionId = 42;
    signupApi.startSignup.and.returnValue(of({ success: true, data: { sessionId: mockSessionId } }));
    
    component.form.setValue({ email: 'contact@boulangerie.fr' });
    component.form.updateValueAndValidity(); 
    component.onSubmit();
    
    expect(signupApi.startSignup).toHaveBeenCalledWith('contact@boulangerie.fr');
    // NOUVEAU MESSAGE PROFESSIONNEL
    expect(toastr.success).toHaveBeenCalledWith('Un code de vérification vous a été envoyé. Veuillez consulter votre boîte mail.');    
    expect(router.navigate).toHaveBeenCalledWith(['/signup/code'], { queryParams: { sessionId: mockSessionId } });
  });

  /**
   * Teste la gestion d'une erreur retournée par le backend.
   */
  it('devrait gérer erreur backend proprement', () => {
    signupApi.startSignup.and.returnValue(throwError(() => new Error('Erreur serveur')));
    
    component.form.setValue({ email: 'contact@boulangerie.fr' });
    component.form.updateValueAndValidity();
    component.onSubmit();
    
    expect(toastr.error).toHaveBeenCalledWith('Erreur lors de l’envoi de l’e-mail.');
    expect(router.navigate).not.toHaveBeenCalled();
  });

  /**
   * Teste l'ignorance d'une soumission si le chargement est actif.
   */
  it('devrait ignorer double clic pendant chargement', () => {
    component.loading = true;
    component.form.setValue({ email: 'contact@boulangerie.fr' });
    component.onSubmit();
    
    expect(signupApi.startSignup).not.toHaveBeenCalled();
  });
});