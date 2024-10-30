import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ApiService } from '../api.service';
import { Observable, of } from 'rxjs';
import { switchMap } from 'rxjs/operators';
import { Scenario, Oggetto, Storia, Alternative, Indovinello } from './scenario.model';

@Component({
  selector: 'app-gioca-storia',
  templateUrl: './gioca-storia.component.html',
  styleUrls: ['./gioca-storia.component.scss']
})
export class GiocaStoriaComponent implements OnInit {
  currentScenario: Scenario | null = null;
  inventory: Oggetto[] = [];
  userRiddleAnswer: string = '';
  storiaId: number = 0;
  storia: Storia | null = null;
  sessioneId: number | null = null;

  constructor(private route: ActivatedRoute, private apiService: ApiService, private router: Router) {}

  ngOnInit(): void {
    // Otteniamo l'ID della storia dall'URL
    this.route.paramMap.pipe(
      switchMap(params => {
        const id = params.get('id');
        if (id !== null) {
          this.storiaId = +id;
          console.log("id storia giocaaaa" + this.storiaId)
          // Carichiamo la sessione esistente se disponibile
          return this.apiService.getSessioneConID(this.storiaId);
        } else {
          return of(null);
        }
      })
    ).subscribe(sessione => {
      if (sessione) {
        this.sessioneId = sessione.idSessione;
        this.inventory = sessione.inventario ? sessione.inventario.oggetti : [];
        this.loadStoryAndScenario();
      } else {
        console.log('Sessione non trovata, creando una nuova sessione per la storia selezionata.');
        this.creaNuovaSessione(); // Metodo per creare una nuova sessione se non esiste
      }
    });
  }

  creaNuovaSessione(): void {
    this.apiService.creaSessione({
      idStoria: this.storiaId,
      username: 'utenteCorrente',
      idScenarioCorrente: 0,  // Assicurati di passare lo scenario iniziale o specifico
      inventario: { oggetti: [] }
    }).subscribe(newSessione => {
      if (newSessione) {
        this.sessioneId = newSessione.idSessione;
        this.inventory = newSessione.inventario.oggetti;

        // Dopo aver creato la sessione, carichiamo il primo scenario utilizzando l'idScenarioCorrente
        this.apiService.getScenario(this.storiaId, newSessione.idScenarioCorrente).subscribe(scenario => {
          if (scenario) {
            this.currentScenario = scenario;
          } else {
            console.error('Errore: scenario iniziale non trovato');
          }
        });
      } else {
        console.error('Errore nella creazione della nuova sessione');
      }
    });
  }

  loadStoryAndScenario(): void {
    this.apiService.getStoriaById(this.storiaId).subscribe(storia => {
      if (storia) {
        this.storia = storia;

        // Verifica che `scenari` sia definito e non vuoto
        if (storia.scenari && storia.scenari.length > 0) {
          const scenarioCorrente = storia.scenari.find((sc: Scenario) => sc.id === (this.sessioneId ? this.sessioneId : 0));

          if (scenarioCorrente) {
            this.currentScenario = scenarioCorrente;
          } else {
            console.error('Scenario iniziale non trovato.');
          }
        } else {
          console.error('Nessuno scenario trovato nella storia.');
        }
      } else {
        console.error('Errore: storia non trovata');
      }
    });
  }

  makeChoice(alternative: Alternative): void {
    if (this.sessioneId && this.currentScenario) {
      this.apiService.elaboraAlternativa(this.sessioneId, this.currentScenario.id, alternative.nextScenarioId).subscribe(response => {
        const nextScenario = this.storia?.scenari.find(sc => sc.id === alternative.nextScenarioId);
        if (nextScenario) {
          this.currentScenario = nextScenario;
          this.userRiddleAnswer = ''; // Reset user answer after making a choice
        } else {
          console.error('Scenario successivo non trovato per ID:', alternative.nextScenarioId);
        }
      });
    }
  }

  submitRiddle(): void {
    if (this.currentScenario && this.currentScenario.indovinelli && this.currentScenario.indovinelli.length > 0 && this.sessioneId) {
      const indovinello = this.currentScenario.indovinelli[0];
      this.apiService.elaboraIndovinello(this.sessioneId, this.currentScenario.id, this.userRiddleAnswer).subscribe(response => {
        const isCorrect = response.esito;
        if (isCorrect) {
          console.log('Risposta corretta!');
          const nextScenario = this.storia?.scenari.find(sc => sc.id === indovinello.idScenarioRispGiusta);
          if (nextScenario) {
            this.currentScenario = nextScenario;
          } else {
            console.error('Scenario successivo non trovato per ID:', indovinello.idScenarioRispGiusta);
          }
        } else {
          console.log('Risposta errata:', this.userRiddleAnswer);
        }
        this.userRiddleAnswer = '';
      });
    }
  }

  collectItem(item: Oggetto): void {
    if (this.sessioneId) {
      this.apiService.raccogliOggetto(this.sessioneId, item.nome).subscribe(() => {
        this.inventory.push(item);
        if (this.currentScenario && this.currentScenario.oggetti) {
          this.currentScenario.oggetti = this.currentScenario.oggetti.filter(i => i.id !== item.id);
        }
      });
    }
  }

  terminaSessione(): void {
        console.log('Sessione terminata');
        this.router.navigate(['/dashboard']);
  }
}
