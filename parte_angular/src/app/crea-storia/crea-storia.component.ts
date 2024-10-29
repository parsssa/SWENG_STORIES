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
  private scenarioCounter = 1; // Contatore incrementale per gli ID degli scenari


  constructor(
    private fb: FormBuilder,
    private apiService: ApiService,
    private router: Router
  ) {
    this.storyForm = this.fb.group({
      title: ['', Validators.required],
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
      item: [''],  // Definisci correttamente `item`
      alternatives: this.fb.array([])  // Array per le alternative
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
      isRiddle: [true],
      correctLeadsTo: [null, Validators.required],  // Aggiungi questo controllo con validazione
      wrongLeadsTo: [null, Validators.required]     // Aggiungi questo controllo con validazione
    });
    this.scenarios.push(scenario);
  }



  addAlternativeToScenario(index: number): void {
    const alternatives = this.scenarios.at(index).get('alternatives') as FormArray;
    const alternative = this.fb.group({
      text: ['', Validators.required],
      leadsTo: [null, Validators.required]  // Definisci `leadsTo` come FormControl
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
    const scenarioOptions = this.scenarios.controls.map((_, index) => ({
      value: index, // Assicurati che `value` sia solo l'indice intero
      label: `Scenario ${index + 1}`
    }));

    const endingOptions = this.endings.controls.map((_, index) => ({
      value: index + this.scenarios.length, // Evita conflitti aggiungendo offset per i finali
      label: `Finale ${index + 1}`
    }));
    return [...scenarioOptions, ...endingOptions];
  }

  onSubmit(): void {

    console.log("FORMONE DATONE", this.storyForm); //LOGGONE FORMONE
    if (this.storyForm.valid) {
      const storyData = this.prepareStoryData();
      console.log("STORIONE DATONE", storyData); //LOGGONE



      /// Invio della storia al backend come JSON
      this.apiService.inserisciStoria(storyData).subscribe(
        response => {
          const idStoria = response.id; // Supponendo che il backend restituisca l'id della storia creata

          // Combina scenari e finali in un unico array
          const allItems = [
            ...this.storyForm.value.scenarios.map((scenario: any) => ({ ...scenario, type: 'scenario' })),
            ...this.storyForm.value.endings.map((ending: any) => ({ ...ending, type: 'ending' }))
          ];

          // Invia ogni scenario o finale al backend come JSON
          allItems.forEach((item: any, index: number) => {
            const scenarioData = this.prepareScenarioData(idStoria, item, index, item.type === 'ending');
            console.log(`Indice attuale ${index}`, scenarioData);

            this.apiService.inserisciScenario(idStoria, scenarioData).subscribe(
              () => console.log(`${item.type === 'ending' ? 'Finale' : 'Scenario'} aggiunto con successo!`),
              error => console.error(`Errore durante l'aggiunta di ${item.type === 'ending' ? 'finale' : 'scenario'}:`, error)
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
    else { //PARTE DEDICATA AGLI ERRORI
      console.log("FORMONE NON VALIDO")
      console.error("Form non valido, errori:", this.storyForm.errors);
      this.storyForm.markAllAsTouched(); // Evidenzia i campi non validi

      Object.keys(this.storyForm.controls).forEach(key => {
        const controlErrors = this.storyForm.get(key)?.errors;
        if (controlErrors) {
          console.log(`Errore nel controllo '${key}':`, controlErrors);
        }
      });

      this.scenarios.controls.forEach((scenario, index) => {
        if (scenario.invalid) {
          console.log(`Errore nello scenario ${index + 1}:`, scenario.errors);
          const scenarioControls = (scenario as FormGroup).controls;
          Object.keys(scenarioControls).forEach(controlName => {
            const errors = scenarioControls[controlName]?.errors;
            if (errors) {
              console.log(`Errore nel controllo '${controlName}' di scenario ${index + 1}:`, errors);
            }
          });
        }
      });

    }
  }

  // Prepara i dati della storia come JSON
  private prepareStoryData() {
    const initialScenario = this.storyForm.value.scenarios[0];

    return {
      titolo: this.storyForm.value.title,
      inizio: initialScenario ? { // Struttura dello scenario iniziale
        idStoria: 0,
        idScenario: 0,
        testoScenario: initialScenario.text,
        oggetto: initialScenario.item || '',
        alternative: initialScenario.alternatives.map((alt: any) => ({
          idScenario: this.scenarioCounter++, // ID INCREMENTALE
          idScenarioSuccessivo: typeof alt.leadsTo === 'object' && alt.leadsTo.index !== undefined ? alt.leadsTo.index : alt.leadsTo,
          testoAlternativa: alt.text,
          oggettoRichiesto: alt.oggettoRichiesto || ''
        })),

        indovinello: initialScenario.isRiddle ? {
          idScenario: this.scenarioCounter++,
          idScenarioRispGiusta: initialScenario.correctLeadsTo, // Collegamento per risposta corretta
          testoIndovinello: initialScenario.riddleQuestion,
          risposta: initialScenario.correctAnswer,
          idScenarioRispSbagliata: initialScenario.wrongLeadsTo, // Collegamento per risposta errata
          tipo: initialScenario.riddleType
        } : null
      } : null,
      id: 0, // ID Arbitario
      username: 'utenteCorrente' // Sostituisci con l'username attuale
    };
  }

  private prepareScenarioData(idStoria: number, item: any, index: number, isEnding: boolean) {
    if (isEnding) {
      return {
        idStoria,
        idScenario: index,
        testoScenario: item.description // Usa il campo description per i finali
      };
    } else {
      return {
        idStoria,
        idScenario: index,
        testoScenario: item.text,
        oggetto: item.item || '',
        // Verifica che alternatives sia un array prima di usare .map()
        alternative: Array.isArray(item.alternatives) ? item.alternatives.map((alt: any) => ({
          testoAlternativa: alt.text,
          idScenarioSuccessivo: typeof alt.leadsTo === 'object' && alt.leadsTo.index !== undefined ? alt.leadsTo.index : alt.leadsTo,
          oggettoRichiesto: alt.oggettoRichiesto || ''
        })) : [], // Se undefined, restituisce un array vuoto
        indovinello: item.isRiddle ? {
          idScenario: index,
          idScenarioRispGiusta: item.correctLeadsTo,
          testoIndovinello: item.riddleQuestion,
          risposta: item.correctAnswer,
          rispostaSbagliata: item.wrongAnswer, // Aggiungi risposta sbagliata
          idScenarioRispSbagliata: item.wrongLeadsTo,
          tipo: item.riddleType
        } : null
      };
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
