// login.component.ts

import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ApiService } from '../api.service';
import { Router } from '@angular/router';
import { authStatus } from '../auth-status'; // Importa il comportamento

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {
  errorMessage: string = ''; // Inizializza errorMessage con una stringa vuota
  loginForm: FormGroup;

  constructor(
    private formBuilder: FormBuilder,
    private apiService: ApiService,
    private router: Router
  ) {
    this.loginForm = this.formBuilder.group({
      username: ['', Validators.required],
      password: ['', Validators.required]
    });
  }

  ngOnInit() {}

  onSubmit() {
    if (this.loginForm.valid) {
      const formData = this.loginForm.value;
      this.apiService.loginUtente(formData).subscribe(
        response => {
          console.log('Login riuscito:', response);
          // Assumi che la risposta contenga un token o qualcosa per identificare l'utente loggato
          // Salva il token e/o le informazioni dell'utente loggato come preferisci, es. in localStorage
          // E poi reindirizza l'utente alla pagina che preferisci, es. dashboard
          authStatus.next(true); // Imposta lo stato di login
          this.router.navigate(['/dashboard']);
        },
        error => {
          console.error('Errore durante il login:', error);
          if (error.error && error.error.message) {
            this.errorMessage = error.error.message;
          } else {
            this.errorMessage = 'Si è verificato un errore durante il login. Riprova più tardi.';
          }
        }
      );
    }
  }
}
