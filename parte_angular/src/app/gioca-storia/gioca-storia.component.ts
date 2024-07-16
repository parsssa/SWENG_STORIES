import { Component, OnInit } from '@angular/core';

interface Scenario {
  title: string;
  description: string;
  riddle?: string;
  riddleType?: 'text' | 'numeric';
  choices?: Choice[];
  itemsToCollect?: string[];
}

interface Choice {
  text: string;
  requiredItem?: string;
  nextScenarioId: number;
}

@Component({
  selector: 'app-gioca-storia',
  templateUrl: './gioca-storia.component.html',
  styleUrls: ['./gioca-storia.component.scss']
})
export class GiocaStoriaComponent implements OnInit {
  currentScenario: Scenario | undefined;
  inventory: string[] = [];
  userRiddleAnswer: string = '';
  scenarios: Scenario[] = [
    {
      title: 'Inizio della storia',
      description: 'Ti trovi in una foresta oscura...',
      choices: [
        { text: 'Vai a destra', nextScenarioId: 1 },
        { text: 'Vai a sinistra', nextScenarioId: 2 }
      ],
      itemsToCollect: ['lanterna']
    },
    {
      title: 'Secondo scenario',
      description: 'Hai trovato una caverna...',
      riddle: 'Qual è il numero magico?',
      riddleType: 'numeric',
      choices: [
        { text: 'Continua', nextScenarioId: 3 }
      ]
    },
    {
      title: 'Terzo scenario',
      description: 'Hai trovato un tesoro...',
      choices: [
        { text: 'Apri il tesoro', nextScenarioId: 4, requiredItem: 'lanterna' }
      ]
    },
    {
      title: 'Fine della storia',
      description: 'Hai completato la storia!',
    }
  ];

  ngOnInit(): void {
    this.loadScenario(0);
  }

  loadScenario(id: number): void {
    if (id >= 0 && id < this.scenarios.length) {
      this.currentScenario = this.scenarios[id];
      console.log('Carica scenario con ID:', id);
    } else {
      console.error('ID scenario non valido:', id);
    }
  }

  canMakeChoice(choice: Choice): boolean {
    if (choice.requiredItem) {
      return this.inventory.includes(choice.requiredItem);
    }
    return true;
  }

  makeChoice(choice: Choice): void {
    if (this.currentScenario) {
      this.loadScenario(choice.nextScenarioId);
    }
  }

  submitRiddle(): void {
    if (this.currentScenario && this.currentScenario.choices && this.currentScenario.choices.length > 0) {
      if (this.currentScenario.riddleType === 'numeric' && this.userRiddleAnswer === '42') {
        console.log('Risposta numerica corretta:', this.userRiddleAnswer);
        this.loadScenario(this.currentScenario.choices[0].nextScenarioId);
      } else if (this.currentScenario.riddleType === 'text' && this.userRiddleAnswer.toLowerCase() === 'answer') {
        console.log('Risposta testuale corretta:', this.userRiddleAnswer);
        this.loadScenario(this.currentScenario.choices[0].nextScenarioId);
      } else {
        console.log('Risposta errata:', this.userRiddleAnswer);
      }
      this.userRiddleAnswer = '';
    }
  }

  collectItem(item: string): void {
    this.inventory.push(item);
    if (this.currentScenario && this.currentScenario.itemsToCollect) {
      this.currentScenario.itemsToCollect = this.currentScenario.itemsToCollect.filter(i => i !== item);
    }
  }
}