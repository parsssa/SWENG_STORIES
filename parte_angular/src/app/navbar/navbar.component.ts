import { Component } from '@angular/core';
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
export class NavbarComponent {
  isLoggedIn = false; // Inizialmente impostato come non autenticato

  constructor() {
    // Simula uno stato di autenticazione fittizio
    this.isLoggedIn = this.checkAuthStatus();
  }


  creaStoria() {
    // Logica per la creazione della storia
    console.log("Funzione creaStoria chiamata.");
  }

  

  logout(): void {
    // Simula la logica di logout
    this.isLoggedIn = false;
  }

  private checkAuthStatus(): boolean {
    // Simula la verifica dello stato di autenticazione
    // Puoi impostare un valore fisso o utilizzare una logica più complessa
    // Per questo esempio, supponiamo che l'utente sia autenticato
    return true;
  }
}