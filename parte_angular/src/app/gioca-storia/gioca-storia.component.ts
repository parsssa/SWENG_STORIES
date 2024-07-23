import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ApiService } from '../api.service';  // Assicurati di avere questo servizio
import { Observable, of } from 'rxjs';
import { switchMap, map } from 'rxjs/operators';

@Component({
  selector: 'app-gioca-storia',
  templateUrl: './gioca-storia.component.html',
  styleUrls: ['./gioca-storia.component.scss']
})
export class GiocaStoriaComponent implements OnInit {
  currentScenario: any;  // Cambia a 'any' se non vuoi definire un'interfaccia
  inventory: string[] = [];
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
          this.storiaId = +id;  // Recupera l'ID dalla route
          return this.apiService.getStoriaById(this.storiaId);  // Recupera la storia dall'API
        } else {
          return of(null);  // Se non c'è un ID, ritorna un osservabile con valore null
        }
      }),
      map(storia => {
        if (storia && storia.inizio) {
          this.loadScenario(storia.inizio.id, storia);
        } else {
          console.error('Storia non trovata o ID non valido:', this.storiaId);
        }
      })
    ).subscribe();
  }

  loadScenario(id: number, storia: any): void {
    const scenario = storia.scenari.find((sc: any) => sc.id === id);
    if (scenario) {
      this.currentScenario = scenario;
    } else {
      console.error('ID scenario non valido:', id);
    }
  }

  canMakeChoice(choice: any): boolean {
    if (choice.requiredItem) {
      return this.inventory.includes(choice.requiredItem);
    }
    return true;
  }

  makeChoice(choice: any): void {
    if (this.currentScenario) {
      const nextScenario = this.currentScenario.choices.find((c: any) => c.text === choice.text);
      if (nextScenario) {
        this.loadScenario(nextScenario.nextScenarioId, this.currentScenario.storia);
      }
    }
  }

  submitRiddle(): void {
    if (this.currentScenario && this.currentScenario.riddleType) {
      if (this.currentScenario.riddleType === 'numeric' && this.userRiddleAnswer === '42') {
        this.makeChoice(this.currentScenario.choices[0]);
      } else if (this.currentScenario.riddleType === 'text' && this.userRiddleAnswer.toLowerCase() === 'answer') {
        this.makeChoice(this.currentScenario.choices[0]);
      } else {
        console.log('Risposta errata:', this.userRiddleAnswer);
      }
      this.userRiddleAnswer = '';
    }
  }

  collectItem(item: string): void {
    this.inventory.push(item);
    if (this.currentScenario && this.currentScenario.itemsToCollect) {
      this.currentScenario.itemsToCollect = this.currentScenario.itemsToCollect.filter((i: string) => i !== item);
    }
  }
}
