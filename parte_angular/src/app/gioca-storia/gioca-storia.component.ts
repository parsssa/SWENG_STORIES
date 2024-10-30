import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ApiService } from '../api.service';
import { Scenario, Oggetto, Storia, Alternativa } from './scenario.model';
import { response } from 'express';

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
  sessioneId: string | null = null;

  constructor(private route: ActivatedRoute, private apiService: ApiService, private router: Router) {}

  ngOnInit(): void {
    const state = history.state;
    if (state.selectedStoria && state.sessioneId) {
      this.storia = state.selectedStoria;
      this.storiaId = state.selectedStoria.id;
      this.sessioneId = state.sessioneId;
      console.log(this.sessioneId);
      this.inventory = state.inventory || [];
      this.currentScenario = this.storia?.inizio || null;
      console.log('Alternative disponibili:', this.currentScenario?.alternative);



      console.log('Storia e sessione ricevute:', this.storia, this.sessioneId, this.currentScenario);
    } else {
      console.error('Errore: nessuna storia o sessione passata allo stato.');
      this.router.navigate(['/seleziona-storia']);
    }
  }

  submitRiddle(): void {
    if (this.currentScenario && this.currentScenario.indovinelli && this.currentScenario.indovinelli.length > 0 && this.sessioneId) {
      const indovinello = this.currentScenario.indovinelli[0];
      this.apiService.elaboraIndovinello(this.sessioneId, this.currentScenario.idScenario, this.userRiddleAnswer).subscribe(response => {
        const isCorrect = response.esito;
        if (isCorrect) {
          const nextScenarioId = indovinello.idScenarioRispGiusta;
          this.apiService.getScenario(this.storiaId, nextScenarioId).subscribe(nextScenario => {
            this.currentScenario = nextScenario;
          });
        }
        this.userRiddleAnswer = '';
      });
    }
  }

  makeChoice(alternative: Alternativa): void {
    if (this.sessioneId && this.currentScenario) {
      console.log("ID MAKE CHOICE: ", alternative.idScenarioSuccessivo);
      this.apiService.elaboraAlternativa(this.sessioneId, alternative.testoAlternativa, alternative.idScenarioSuccessivo).subscribe(response => {
      
      console.log(response);
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
