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
  errorMessage: string = '';
  successMessage: string = '';
  registerForm: FormGroup;

  constructor(
    private formBuilder: FormBuilder,
    private apiService: ApiService,
    private router: Router
  ) {
    this.registerForm = this.formBuilder.group({
      username: ['', Validators.required],
      password: ['', Validators.required],
      confirmPassword: ['', Validators.required]
    }, { validator: this.passwordMatchValidator });
  }

  ngOnInit() {}

  passwordMatchValidator(formGroup: FormGroup) {
    return formGroup.get('password')?.value === formGroup.get('confirmPassword')?.value
      ? null : { 'mismatch': true };
  }

  onSubmit() {
    if (this.registerForm.valid) {
      const formData = this.registerForm.value;
      this.apiService.createUtente({ username: formData.username, password: formData.password }).subscribe(
        response => {
          console.log('Registrazione riuscita:', response);
          this.successMessage = 'Registrazione avvenuta con successo. Reindirizzamento al login...';
          setTimeout(() => {
            this.router.navigate(['/login']);
          }, 2000); // 2 secondi di ritardo
        },
        error => {
          console.error('Errore durante la registrazione:', error);
          if (error.status === 409) { // HTTP 409 Conflict
            this.errorMessage = 'Username già esistente. Si prega di effettuare il login.';
          } else {
            this.errorMessage = error.error.message || 'Si è verificato un errore durante la registrazione. Riprova più tardi.';
          }
        }
      );
    }
  }
}