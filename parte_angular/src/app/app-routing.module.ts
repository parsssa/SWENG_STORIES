import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './home/home.component';
import { LoginComponent } from './login/login.component';
import { RegisterComponent } from './register/register.component';
import { CreaStoriaComponent } from './crea-storia/crea-storia.component';
import { GiocaStoriaComponent } from './gioca-storia/gioca-storia.component';

const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'creaStoria', component: CreaStoriaComponent},
  { path: 'giocaStoria', component: GiocaStoriaComponent},

  // Altre rotte qui...
  { path: '**', redirectTo: '' } // Rotta wildcard
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }