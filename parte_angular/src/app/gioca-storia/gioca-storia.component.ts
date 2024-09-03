import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ApiService } from '../api.service';
import { Observable, of } from 'rxjs';
import { switchMap } from 'rxjs/operators';
import { Scenario, Oggetto, Storia, Alternative } from './scenario.model'; // Assicurati che scenario.model.ts contenga la definizione di Alternative

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
      if (storia) {
        this.storia = storia;
        this.currentScenario = storia.inizio;
        this.inventory = storia.inventario ? storia.inventario.oggetti : [];
      }
    });
  }

  makeChoice(alternative: Alternative): void {  // Modificato per gestire l'Alternative direttamente
    if (this.storia && alternative) {
      const nextScenario = this.storia.scenari.find((sc: Scenario) => sc.id === alternative.nextScenarioId);
      if (nextScenario) {
        this.currentScenario = nextScenario;
      } else {
        console.error('Scenario successivo non trovato per ID:', alternative.nextScenarioId);
      }
    }
  }

  submitRiddle(): void {
    if (this.currentScenario && this.currentScenario.indovinelli.length > 0) {
      const indovinello = this.currentScenario.indovinelli[0];
      let chosenAlternative: Alternative | undefined;
  
      if (indovinello.tipo === 'numerico' && this.userRiddleAnswer === indovinello.rispostaCorretta.toString()) {
        chosenAlternative = this.currentScenario.alternatives.find(alt => alt.type === 'indovinello');
      } else if (indovinello.tipo === 'testuale' && this.userRiddleAnswer.toLowerCase() === indovinello.rispostaCorretta.toLowerCase()) {
        chosenAlternative = this.currentScenario.alternatives.find(alt => alt.type === 'indovinello');
      } else {
        console.log('Risposta errata:', this.userRiddleAnswer);
      }
  
      if (chosenAlternative) {
        this.makeChoice(chosenAlternative);
      } else {
        console.log('Nessuna alternativa corrispondente trovata.');
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
