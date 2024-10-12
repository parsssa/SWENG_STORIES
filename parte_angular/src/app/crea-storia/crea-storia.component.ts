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
      scenarios: this.fb.array([]),
      endings: this.fb.array([]),
      riddle: [''],
      riddleType: ['text'],
      riddleQuestion: [''], 
      riddleAnswer: [''],
      inventory: ['']
    });
  }

  ngOnInit(): void {
    this.addScenario(); // Add one default scenario
  }

  get scenarios(): FormArray {
    return this.storyForm.get('scenarios') as FormArray;
  }

  addScenario(): void {
    const scenario = this.fb.group({
      text: ['', Validators.required],
      alternatives: this.fb.array([]),
      items: ['']
    });
    this.scenarios.push(scenario);
    this.addAlternativeToScenario(this.scenarios.length - 1); // Add one default alternative
  }

  get endings(): FormArray {
    return this.storyForm.get('endings') as FormArray;
  }

  addEnding(): void {
    const ending = this.fb.group({
      description: ['', Validators.required],
      order: ['']
    });
    this.endings.push(ending);
  }

  addAlternativeToScenario(index: number): void {
    const alternatives = (this.scenarios.at(index).get('alternatives') as FormArray);
    const alternative = this.fb.group({
      text: ['', Validators.required],
      type: ['without-items', Validators.required],
      items: [''],
      leadsTo: [null]  // Collega lo scenario successivo
    });
    alternatives.push(alternative);
  }
  

  removeScenario(index: number): void {
    this.scenarios.removeAt(index);
  }

  getAlternatives(scenarioIndex: number): FormArray {
    const scenarios = this.storyForm.get('scenarios') as FormArray;
    const scenario = scenarios.at(scenarioIndex) as FormGroup;
    return scenario.get('alternatives') as FormArray;
  }

  getScenarioOptions(): any[] {
    // Usa l'ID reale dello scenario (numerico) per `leadsTo`
    const scenarioOptions = this.scenarios.controls.map((control, index) => ({
      value: index,  // Qui utilizziamo l'indice numerico come valore
      label: `Scenario ${index + 1}`
    }));
  
    const endingOptions = this.endings.controls.map((control, index) => ({
      value: `ending-${index}`,  // I finali possono ancora usare questa stringa
      label: `Finale ${index + 1}`
    }));
  
    return [...scenarioOptions, ...endingOptions];
  }
  
  
  

  trackByFn(index: number, item: any): number {
    return index; // or item.value depending on your data structure
  }
  
  onSubmit(): void {
    if (this.storyForm.valid) {
      // Ottieni i dati del form come oggetto JSON
      const storyData = this.storyForm.value;
  
      // Stampa i dati della storia nella console (opzionale per debug)
      console.log('Storia inviata:', JSON.stringify(storyData, null, 2));
  
      // Invio dei dati al backend come JSON
      this.apiService.createStoria(storyData).subscribe(
        (response) => {
          //this.router.navigate(['/success-page']);  // Redirect alla pagina di successo
        },
        (error) => {
          console.error('Errore durante la creazione della storia', error);
        }
      );
    } else {
      console.error('Il form non è valido');
    }
  }
  

  resetForm(): void {
    this.storyForm.reset();
    this.scenarios.clear();
    this.endings.clear();
    this.addScenario(); // Add one default scenario again
    this.addEnding(); // Add one default ending again
  }
}
