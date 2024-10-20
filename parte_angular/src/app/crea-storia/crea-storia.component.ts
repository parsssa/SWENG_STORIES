import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, FormArray, Validators } from '@angular/forms';

@Component({
  selector: 'app-crea-storia',
  templateUrl: './crea-storia.component.html',
  styleUrls: ['./crea-storia.component.scss']
})
export class CreaStoriaComponent implements OnInit {
  storyForm: FormGroup;

  constructor(private fb: FormBuilder) {
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
      text: ['', Validators.required],  // Descrizione dello scenario
      riddleQuestion: ['', Validators.required],  // Domanda dell'indovinello
      riddleType: ['text', Validators.required],  // Tipo di indovinello
      correctAnswer: ['', Validators.required],  // Risposta corretta
      wrongAnswer: ['', Validators.required],  // Risposta errata
      isRiddle: [true]  // Indica che questo scenario contiene un indovinello
    });
    this.scenarios.push(scenario);
  }

  addAlternativeToScenario(index: number): void {
    const alternatives = (this.scenarios.at(index).get('alternatives') as FormArray);
    const alternative = this.fb.group({
      text: ['', Validators.required],  // Descrizione dell'alternativa
      leadsTo: [null]  // Collegamento allo scenario successivo o finale
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
      const storyData = this.storyForm.value;
      console.log(storyData); // Sostituire con la logica di invio dati
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
