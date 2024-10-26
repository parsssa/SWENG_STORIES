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
  errorMessage: string = '';

  constructor(
    private fb: FormBuilder,
    private apiService: ApiService,
    private router: Router
  ) {
    this.storyForm = this.fb.group({
      title: ['', Validators.required],
      description: ['', Validators.required],
      start: ['', Validators.required],
      scenarios: this.fb.array([]),
      endings: this.fb.array([])
    });
  }

  ngOnInit(): void {
    this.addScenarioWithAlternatives(); // Aggiungi uno scenario predefinito con alternative
  }

  get scenarios(): FormArray {
    return this.storyForm.get('scenarios') as FormArray;
  }

  get endings(): FormArray {
    return this.storyForm.get('endings') as FormArray;
  }

  isRiddleScenario(index: number): boolean {
    return this.scenarios.at(index).get('isRiddle')?.value || false;
  }

  addScenarioWithAlternatives(): void {
    const scenario = this.fb.group({
      text: ['', Validators.required],
      item: [''],
      alternatives: this.fb.array([]) // Array per le alternative
    });
    this.scenarios.push(scenario);
    this.addAlternativeToScenario(this.scenarios.length - 1); // Aggiungi una prima alternativa
  }

  addScenarioWithRiddle(): void {
    const scenario = this.fb.group({
      text: ['', Validators.required],
      riddleQuestion: ['', Validators.required],
      riddleType: ['text', Validators.required],
      correctAnswer: ['', Validators.required],
      wrongAnswer: ['', Validators.required],
      isRiddle: [true]
    });
    this.scenarios.push(scenario);
  }

  addAlternativeToScenario(index: number): void {
    const alternatives = (this.scenarios.at(index).get('alternatives') as FormArray);
    const alternative = this.fb.group({
      text: ['', Validators.required],
      leadsTo: [null]
    });
    alternatives.push(alternative);
  }

  getAlternatives(scenarioIndex: number): FormArray {
    return this.scenarios.at(scenarioIndex).get('alternatives') as FormArray;
  }

  addEnding(): void {
    const ending = this.fb.group({
      description: ['', Validators.required]
    });
    this.endings.push(ending);
  }

  getScenarioOptions(): any[] {
    const scenarioOptions = this.scenarios.controls.map((control, index) => ({
      value: index,
      label: `Scenario ${index + 1}`
    }));
  
    const endingOptions = this.endings.controls.map((control, index) => ({
      value: `ending-${index}`,
      label: `Finale ${index + 1}`
    }));
  
    return [...scenarioOptions, ...endingOptions];
  }

  onSubmit(): void {
    if (this.storyForm.valid) {
      const storyData = {
        title: this.storyForm.value.title,
        description: this.storyForm.value.description,
        start: this.storyForm.value.start
      };
      
      this.apiService.inserisciStoria(storyData).subscribe(
        response => {
          const idStoria = response.id; // Supponendo che il backend restituisca l'id della storia creata

          // Invia ogni scenario
          this.storyForm.value.scenarios.forEach((scenario: any) => {
            this.apiService.inserisciScenario(idStoria, scenario).subscribe(
              () => console.log('Scenario aggiunto con successo!'),
              error => console.error('Errore durante l\'aggiunta dello scenario:', error)
            );
          });

          console.log('Storia creata con successo!', response);
          this.router.navigate(['/dashboard']); // Reindirizza dopo la creazione
        },
        error => {
          console.error('Errore durante la creazione della storia:', error);
          this.errorMessage = 'Si è verificato un errore durante la creazione della storia. Riprova più tardi.';
        }
      );
    }
  }

  resetForm(): void {
    this.storyForm.reset();
    this.scenarios.clear();
    this.endings.clear();
    this.addScenarioWithAlternatives(); // Aggiungi uno scenario predefinito
  }

  trackByFn(index: number): number {
    return index;
  }
}
