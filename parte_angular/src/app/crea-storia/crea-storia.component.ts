// crea-storia.component.ts

import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, FormArray, Validators } from '@angular/forms';
import { ApiService } from '../api.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-crea-storia',
  templateUrl: './crea-storia.component.html',
  styleUrls: ['./crea-storia.component.scss']
})
export class CreaStoriaComponent implements OnInit {
  storyForm: FormGroup;

  constructor(private fb: FormBuilder, private apiService: ApiService, private router: Router) {
    this.storyForm = this.fb.group({
      title: ['', Validators.required],
      description: ['', Validators.required],
      start: ['', Validators.required],
      alternatives: this.fb.array([]),
      endings: this.fb.array([]),
      riddle: [''],
      riddleType: ['text'],
      inventory: ['']
    });
  }

  ngOnInit(): void {
    this.addAlternative(); // Aggiungi una prima alternativa per default
    this.addEnding(); // Aggiungi un primo finale per default
  }

  get alternatives(): FormArray {
    return this.storyForm.get('alternatives') as FormArray;
  }

  get endings(): FormArray {
    return this.storyForm.get('endings') as FormArray;
  }

  addAlternative(): void {
    const alternative = this.fb.group({
      text: ['', Validators.required],
      type: ['without-items', Validators.required],
      items: ['']
    });
    this.alternatives.push(alternative);
  }

  addEnding(): void {
    const ending = this.fb.control('', Validators.required);
    this.endings.push(ending);
  }

  onSubmit(): void {
    if (this.storyForm.valid) {
      const storyData = this.storyForm.value;
      this.apiService.createStoria(storyData).subscribe(
        response => {
          // Naviga alla lista delle storie o a un'altra pagina di conferma
          this.router.navigate(['/storie']);
        },
        error => {
          console.error('Errore nella creazione della storia:', error);
        }
      );
    }
  }

  resetForm(): void {
    this.storyForm.reset();
    while (this.alternatives.length) {
      this.alternatives.removeAt(0);
    }
    while (this.endings.length) {
      this.endings.removeAt(0);
    }
    this.addAlternative();
    this.addEnding();
  }
}