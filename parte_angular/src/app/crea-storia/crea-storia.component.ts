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
      riddleQuestion: [''], // Campo aggiunto per la domanda dell'indovinello
      riddleAnswer: [''], // Campo aggiunto per la risposta dell'indovinello
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

      const formData = new FormData();
      formData.append('titolo', storyData.title);
      formData.append('descrizione', storyData.description);
      formData.append('inizioDescrizione', storyData.start);
      formData.append('riddle', storyData.riddle);
      formData.append('riddleType', storyData.riddleType);
      formData.append('riddleQuestion', storyData.riddleQuestion);
      formData.append('riddleAnswer', storyData.riddleAnswer);
      formData.append('inventory', storyData.inventory);

      storyData.endings.forEach((ending: string, index: number) => {
        formData.append(`finali[${index}].descrizione`, ending);
      });

      storyData.alternatives.forEach((alt: any, index: number) => {
        formData.append(`scenari[${index}].descrizione`, alt.text);
        if (alt.items) {
          alt.items.split(',').forEach((item: string, itemIndex: number) => {
            formData.append(`scenari[${index}].oggetti[${itemIndex}].nome`, item.trim());
          });
        }
      });

      this.apiService.createStoria(formData).subscribe(
        response => {
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
