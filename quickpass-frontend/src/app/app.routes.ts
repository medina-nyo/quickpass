import { Routes } from '@angular/router';
import { SignupEmailComponent } from './features/signup/pages/signup-email/signup-email';

export const routes: Routes = [
  { path: 'signup/email', component: SignupEmailComponent },
  { path: '', redirectTo: 'signup/email', pathMatch: 'full' }
];
