import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ApiService } from '../api.service';
import { Observable, of } from 'rxjs';
import { switchMap } from 'rxjs/operators';
import { Scenario, Oggetto, Alternative } from './scenario.model'; // Percorso relativo aggiornato

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

  constructor(
    private route: ActivatedRoute,
    private apiService: ApiService
  ) {}

  ngOnInit(): void {
    this.route.paramMap.pipe(
      switchMap(params => {
        const id = params.get('id');
        if (id !== null) {
          this.storiaId = +id;
          return this.apiService.getStoriaById(this.storiaId);
        } else {
          return of(null);
        }
      })
    ).subscribe(storia => {
      if (storia && storia.inizio) {
        console.log('Storia caricata:', storia);
        this.loadScenario(storia.inizio.id, storia);
      } else {
        console.error('Storia non trovata o ID non valido:', this.storiaId);
      }
    });
  }

  loadScenario(id: number, storia: any): void {
    const scenario = storia.scenari.find((sc: Scenario) => sc.id === id);
    if (scenario) {
      console.log('Scenario caricato:', scenario); // Debug: stampa lo scenario caricato
      this.currentScenario = scenario;
    } else {
      console.error('ID scenario non valido:', id);
    }
  }

  canMakeChoice(choice: Alternative): boolean {
    if (choice.requiredItem) {
      return this.inventory.some(item => item.nome === choice.requiredItem);
    }
    return true;
  }

  makeChoice(choice: Alternative): void {
    if (this.currentScenario) {
      const nextScenario = this.currentScenario.alternative.find((c: Alternative) => c.text === choice.text);
      if (nextScenario) {
        this.loadScenario(nextScenario.nextScenarioId, this.currentScenario);
      }
    }
  }

  submitRiddle(): void {
    if (this.currentScenario && this.currentScenario.indovinelli.length > 0) {
      const indovinello = this.currentScenario.indovinelli[0]; // Supponiamo che ci sia un solo indovinello per scenario
      if (indovinello.tipo === 'numerico' && this.userRiddleAnswer === '42') {
        this.makeChoice(this.currentScenario.alternative[0]);
      } else if (indovinello.tipo === 'testuale' && this.userRiddleAnswer.toLowerCase() === 'answer') {
        this.makeChoice(this.currentScenario.alternative[0]);
      } else {
        console.log('Risposta errata:', this.userRiddleAnswer);
      }
      this.userRiddleAnswer = '';
    }
  }

  collectItem(item: Oggetto): void {
    this.inventory.push(item);
    if (this.currentScenario) {
      this.currentScenario.oggetti = this.currentScenario.oggetti.filter((i: Oggetto) => i.id !== item.id);
    }
  }
}
