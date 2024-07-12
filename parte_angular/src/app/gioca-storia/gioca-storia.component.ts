import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-gioca-storia',
  templateUrl: './gioca-storia.component.html',
  styleUrls: ['./gioca-storia.component.scss']
})
export class GiocaStoriaComponent implements OnInit {
  story: any;
  currentScenario: any;
  inventory: any[] = [];
  storyId: number = 1;
  riddleAnswer: string = '';

  constructor() { }

  ngOnInit(): void {
    this.loadStory();
  }

  loadStory(): void {
    // Dati statici per simulare una storia
    this.story = {
      id: 1,
      title: "L'avventura nella foresta incantata",
      startScenario: {
        title: "L'ingresso della foresta",
        description: "Ti trovi all'ingresso di una foresta misteriosa. Cosa fai?",
        choices: [
          { text: "Entra nella foresta", nextScenario: "foresta" },
          { text: "Torna indietro", nextScenario: "inizio" }
        ]
      }
    };
    this.currentScenario = this.story.startScenario;
  }

  makeDecision(decision: any): void {
    // Simulazione di una decisione
    console.log("Decisione presa:", decision);
    if (decision.nextScenario === "foresta") {
      this.currentScenario = {
        title: "Nel cuore della foresta",
        description: "Sei circondato da alberi altissimi e suoni misteriosi.",
        choices: [
          { text: "Esplora più a fondo", nextScenario: "esplora" },
          { text: "Torna all'ingresso", nextScenario: "inizio" }
        ]
      };
    } else {
      this.currentScenario = this.story.startScenario;
    }
    
    // Aggiungi un oggetto all'inventario per simulazione
    this.inventory.push({ name: "Mappa misteriosa" });
  }
}