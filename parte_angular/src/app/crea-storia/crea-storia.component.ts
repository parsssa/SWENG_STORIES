import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, FormArray, Validators } from '@angular/forms';
import { ApiService } from '../api.service';  // Assicurati che il percorso sia corretto
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
      scenarios: this.fb.array([]),
      endings: this.fb.array([]),
      globalRiddle: [''],
      globalRiddleType: ['text'],
      globalRiddleQuestion: [''],
      globalRiddleAnswer: [''],
      inventory: [''],
    });
  }

  ngOnInit(): void {
    this.addScenario(); // Aggiungi un primo scenario per default
    this.addEnding(); // Aggiungi un primo finale per default
  }

  get scenarios(): FormArray {
    return this.storyForm.get('scenarios') as FormArray;
  }

  get endings(): FormArray {
    return this.storyForm.get('endings') as FormArray;
  }

  getAlternatives(scenarioIndex: number): FormArray {
    return this.scenarios.at(scenarioIndex).get('alternatives') as FormArray;
  }

  addScenario(): void {
    const scenario = this.fb.group({
      description: ['', Validators.required],
      alternatives: this.fb.array([]),
      riddle: [''],
      riddleType: ['text'],
      riddleQuestion: [''],
      riddleAnswer: [''],
      inventory: [''],
    });
    this.scenarios.push(scenario);
    this.addAlternative(this.scenarios.length - 1);  // Aggiungi una alternativa per lo scenario appena creato
  }

  addAlternative(scenarioIndex: number): void {
    const alternative = this.fb.group({
      text: ['', Validators.required],
      type: ['without-items', Validators.required],
      items: ['']
    });
    this.getAlternatives(scenarioIndex).push(alternative);
  }

  addEnding(): void {
    const ending = this.fb.group({
      description: ['', Validators.required],
      order: ['']
    });
    this.endings.push(ending);
  }

  onSubmit(): void {
    if (this.storyForm.valid) {
      const storyData = this.storyForm.value;
  
      const formData = new FormData();
      formData.append('titolo', storyData.title);
      formData.append('descrizione', storyData.description);
      formData.append('inizioDescrizione', storyData.start);

      storyData.scenarios.forEach((scenario: any, scenarioIndex: number) => {
        formData.append(`scenari[${scenarioIndex}].descrizione`, scenario.description);

        scenario.alternatives.forEach((alt: any, altIndex: number) => {
          formData.append(`scenari[${scenarioIndex}].alternatives[${altIndex}].text`, alt.text);
          formData.append(`scenari[${scenarioIndex}].alternatives[${altIndex}].type`, alt.type);
          formData.append(`scenari[${scenarioIndex}].alternatives[${altIndex}].items`, alt.items || ''); // Gestione caso in cui items può essere undefined
        });

        formData.append(`scenari[${scenarioIndex}].riddle`, scenario.riddle || ''); // Gestione caso in cui riddle può essere undefined
        formData.append(`scenari[${scenarioIndex}].riddleType`, scenario.riddleType || '');
        formData.append(`scenari[${scenarioIndex}].riddleQuestion`, scenario.riddleQuestion || '');
        formData.append(`scenari[${scenarioIndex}].riddleAnswer`, scenario.riddleAnswer || '');
        formData.append(`scenari[${scenarioIndex}].inventory`, scenario.inventory || '');
      });

      storyData.endings.forEach((ending: any, endingIndex: number) => {
        formData.append(`finali[${endingIndex}].descrizione`, ending.description);
        formData.append(`finali[${endingIndex}].ordine`, ending.order || ''); // Gestione caso in cui order può essere undefined
      });

      // Aggiungi indovinello globale se presente
      formData.append('globalRiddle', storyData.globalRiddle || '');
      formData.append('globalRiddleType', storyData.globalRiddleType || '');
      formData.append('globalRiddleQuestion', storyData.globalRiddleQuestion || '');
      formData.append('globalRiddleAnswer', storyData.globalRiddleAnswer || '');
      formData.append('inventory', storyData.inventory || '');

      this.apiService.createStoria(formData).subscribe(
        response => {
          console.log('Storia creata con successo!', response);
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
    this.scenarios.clear();
    this.endings.clear();
    this.addScenario();
    this.addEnding();
  }
}