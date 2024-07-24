import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ApiService } from '../api.service';
import { Observable, of } from 'rxjs';
import { switchMap } from 'rxjs/operators';
import { Scenario, Oggetto, Storia } from './scenario.model'; // Assicurati che scenario.model.ts contenga la definizione di Storia

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
  storia: Storia | null = null; // Aggiunto per tenere traccia dell'intera storia

  constructor(
    private route: ActivatedRoute,
    private apiService: ApiService
  ) {}

  ngOnInit(): void {
    console.log('ngOnInit chiamato');
    this.route.paramMap.pipe(
      switchMap(params => {
        const id = params.get('id');
        if (id !== null) {
          this.storiaId = +id;
          console.log('ID storia trovato:', this.storiaId);
          return this.apiService.getStoriaById(this.storiaId);
        } else {
          console.log('ID storia non trovato');
          return of(null);
        }
      })
    ).subscribe(storia => {
      if (storia) {
        this.storia = storia;
        console.log('Storia caricata:', storia);
        this.currentScenario = storia.inizio;
        console.log('Scenario iniziale:', this.currentScenario);
        this.inventory = storia.inventario ? storia.inventario.oggetti : [];
        console.log('Inventario iniziale:', this.inventory);
      } else {
        console.error('Storia non trovata o ID non valido:', this.storiaId);
      }
    });
  }

  canMakeChoice(nextScenarioId: number): boolean {
    // Logica per verificare se una scelta può essere fatta (se necessario)
    return true;
  }

  makeChoice(nextScenarioId: number): void {
    console.log('Scelta effettuata, prossimo scenario ID:', nextScenarioId);
    if (this.storia) {
      const nextScenario = this.storia.scenari.find((sc: Scenario) => sc.id === nextScenarioId);
      if (nextScenario) {
        console.log('Scenario successivo trovato:', nextScenario);
        this.currentScenario = nextScenario;
      } else {
        console.error('Scenario successivo non trovato per ID:', nextScenarioId);
      }
    }
  }

  submitRiddle(): void {
    console.log('Risposta indovinello inviata:', this.userRiddleAnswer);
    if (this.currentScenario && this.currentScenario.indovinelli.length > 0) {
      const indovinello = this.currentScenario.indovinelli[0]; // Supponiamo che ci sia un solo indovinello per scenario
      console.log('Indovinello:', indovinello);
      if (indovinello.tipo === 'numerico' && this.userRiddleAnswer === indovinello.rispostaCorretta.toString()) {
        console.log('Risposta corretta per indovinello numerico');
        this.makeChoice(indovinello.scenarioId);
      } else if (indovinello.tipo === 'testuale' && this.userRiddleAnswer.toLowerCase() === indovinello.rispostaCorretta.toLowerCase()) {
        console.log('Risposta corretta per indovinello testuale');
        this.makeChoice(indovinello.scenarioId);
      } else {
        console.log('Risposta errata:', this.userRiddleAnswer);
      }
      this.userRiddleAnswer = '';
    }
  }

  collectItem(item: Oggetto): void {
    console.log('Oggetto raccolto:', item);
    this.inventory.push(item);
    if (this.currentScenario) {
      this.currentScenario.oggetti = this.currentScenario.oggetti.filter((i: Oggetto) => i.id !== item.id);
      console.log('Inventario aggiornato:', this.inventory);
      console.log('Oggetti rimanenti nello scenario:', this.currentScenario.oggetti);
    }
  }
}
