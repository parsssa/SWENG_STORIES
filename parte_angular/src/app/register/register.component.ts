// register.component.ts

import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ApiService } from '../api.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent implements OnInit {
  errorMessage: string = ''; // Inizializza errorMessage con una stringa vuota
  registerForm: FormGroup;

  constructor(
    private formBuilder: FormBuilder,
    private apiService: ApiService,
    private router: Router
  ) {
    this.registerForm = this.formBuilder.group({
      username: ['', Validators.required],
      password: ['', Validators.required]
    });
  }

  ngOnInit() {}

  onSubmit() {
    if (this.registerForm.valid) {
      const formData = this.registerForm.value;
      this.apiService.createUtente(formData).subscribe(
        response => {
          console.log('Registrazione riuscita:', response);
          this.router.navigate(['/login']);
        },
        error => {
          console.error('Errore durante la registrazione:', error);
          if (error.error && error.error.message) {
            // Se l'API restituisce un messaggio di errore personalizzato
            this.errorMessage = error.error.message;
          } else {
            // Se l'API non restituisce un messaggio di errore personalizzato,
            // visualizza un messaggio di errore generico
            this.errorMessage = 'Si è verificato un errore durante la registrazione. Riprova più tardi.';
          }
        }
      );
    }
  }
  
}