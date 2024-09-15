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
      leadsTo: [null]  // Link to other scenario
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
    const scenarioOptions = this.scenarios.controls.map((control, index) => ({
      value: `scenario-${index}`,
      label: `Scenario ${index + 1}`
    }));
  
    const endingOptions = this.endings.controls.map((control, index) => ({
      value: `ending-${index}`,
      label: `Finale ${index + 1}`
    }));
  
    return [...scenarioOptions, ...endingOptions];
  }
  

  trackByFn(index: number, item: any): number {
    return index; // or item.value depending on your data structure
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

      storyData.scenarios.forEach((scenario: any, index: number) => {
        formData.append(`scenari[${index}].descrizione`, scenario.text);
        if (scenario.items) {
          scenario.items.split(',').forEach((item: string, itemIndex: number) => {
            formData.append(`scenari[${index}].oggetti[${itemIndex}].nome`, item.trim());
          });
        }
        scenario.alternatives.forEach((alt: any, altIndex: number) => {
          formData.append(`scenari[${index}].alternatives[${altIndex}].descrizione`, alt.text);
          formData.append(`scenari[${index}].alternatives[${altIndex}].tipo`, alt.type);
          if (alt.items) {
            alt.items.split(',').forEach((item: string, itemIndex: number) => {
              formData.append(`scenari[${index}].alternatives[${altIndex}].oggetti[${itemIndex}].nome`, item.trim());
            });
          }
          if (alt.leadsTo != null) {
            formData.append(`scenari[${index}].alternatives[${altIndex}].portaA`, alt.leadsTo);
          }
        });
      });

      storyData.endings.forEach((ending: any, endingIndex: number) => {
        formData.append(`finali[${endingIndex}].descrizione`, ending.description);
      });

      this.apiService.createStoria(formData).subscribe(
        (response) => {
          this.router.navigate(['/success-page']); // or any desired page
        },
        (error) => {
          console.error('Error creating story', error);
        }
      );
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
