import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ApiService } from '../api.service';
import { Observable, of } from 'rxjs';
import { switchMap } from 'rxjs/operators';
import { Scenario, Oggetto, Storia, Alternative } from './scenario.model';

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

  constructor(private route: ActivatedRoute, private apiService: ApiService) {}

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
        // Cerca il primo scenario nella lista di scenari
        if (storia.scenari && storia.scenari.length > 0) {
          this.currentScenario = storia.scenari[0];  // Carica il primo scenario
        } else {
          console.error('Nessuno scenario trovato nella storia.');
        }
        this.inventory = storia.inventario ? storia.inventario.oggetti : [];
      }
    });
  }

  makeChoice(alternative: Alternative): void {
    if (this.storia && alternative) {
      const nextScenario = this.storia.scenari.find(sc => sc.id === alternative.nextScenarioId);
      if (nextScenario) {
        this.currentScenario = nextScenario;
        this.userRiddleAnswer = ''; // Reset user answer after making a choice
      } else {
        console.error('Scenario successivo non trovato per ID:', alternative.nextScenarioId);
      }
    }
  }

  submitRiddle(): void {
    if (this.currentScenario && this.currentScenario.indovinelli.length > 0) {
      const indovinello = this.currentScenario.indovinelli[0];
      let isCorrect = false;

      // Verifica risposta in base al tipo di indovinello
      if (indovinello.tipo === 'numerico' && this.userRiddleAnswer === indovinello.rispostaCorretta.toString()) {
        isCorrect = true;
      } else if (indovinello.tipo === 'testuale' && this.userRiddleAnswer.toLowerCase() === indovinello.rispostaCorretta.toLowerCase()) {
        isCorrect = true;
      }

      if (isCorrect) {
        console.log('Risposta corretta!');
        const chosenAlternative = this.currentScenario.alternatives.find(alt => alt.type === 'indovinello');
        if (chosenAlternative) {
          this.makeChoice(chosenAlternative);
        }
      } else {
        console.log('Risposta errata:', this.userRiddleAnswer);
      }

      this.userRiddleAnswer = ''; // Reset dell'input
    }
  }

  collectItem(item: Oggetto): void {
    this.inventory.push(item);
    if (this.currentScenario) {
      this.currentScenario.oggetti = this.currentScenario.oggetti.filter(i => i.id !== item.id);
    }
  }
}
