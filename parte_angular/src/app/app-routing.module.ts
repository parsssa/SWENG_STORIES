import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './home/home.component';
import { LoginComponent } from './login/login.component';
import { RegisterComponent } from './register/register.component';
import { CreaStoriaComponent } from './crea-storia/crea-storia.component';
import { GiocaStoriaComponent } from './gioca-storia/gioca-storia.component';
import { SelezioneStoriaComponent } from './selezione-storia/selezione-storia.component';
import { ModificaStoriaComponent } from './modifica-storia/modifica-storia.component';

const routes: Routes = [
  // Rotte principali
  { path: '', component: HomeComponent },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'creaStoria', component: CreaStoriaComponent},
  { path: 'giocaStoria/:id', component: GiocaStoriaComponent },
  { path: 'selezione-storia', component: SelezioneStoriaComponent }, // Nuova rotta per selezione storia
  { path: 'modifica-storia/:id', component: ModificaStoriaComponent }, // Nuova rotta per modifica storia
  
  // Rotta wildcard per gestire URL non trovati
  { path: '**', redirectTo: '' } // Rotta wildcard
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
