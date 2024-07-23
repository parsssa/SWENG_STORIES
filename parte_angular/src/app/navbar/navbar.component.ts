// navbar.component.ts

import { Component, OnInit, OnDestroy } from '@angular/core';
import { authStatus } from '../auth-status'; // Importa il comportamento
import { Subscription } from 'rxjs';
import { trigger, state, style, animate, transition } from '@angular/animations';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss'],
  animations: [
    trigger('fade', [
      state('in', style({ opacity: 1 })),
      transition(':enter', [style({ opacity: 0 }), animate(600)]),
      transition(':leave', animate(600, style({ opacity: 0 })))
    ])
  ]
})
export class NavbarComponent implements OnInit, OnDestroy {
  isLoggedIn = false;
  private subscription?: Subscription; // Dichiara la proprietà come opzionale

  ngOnInit(): void {
    this.subscription = authStatus.subscribe(status => {
      this.isLoggedIn = status;
    });
  }

  ngOnDestroy(): void {
    // Pulisci l'iscrizione quando il componente viene distrutto
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
  }

  creaStoria() {
    // Logica per la creazione della storia
    console.log("Funzione creaStoria chiamata.");
  }

  logout(): void {
    authStatus.next(false); // Aggiorna lo stato di autenticazione
  }
}
