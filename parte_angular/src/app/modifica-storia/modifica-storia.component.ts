// modifica-storia.component.ts

import { Component, OnInit } from '@angular/core';
import { ApiService } from '../api.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-modifica-storia',
  templateUrl: './modifica-storia.component.html',
  styleUrls: ['./modifica-storia.component.scss']
})
export class ModificaStoriaComponent implements OnInit {
  storie: any[] = [];
  selectedStoria: any = null;
  scenarioDescriptions: string[] = []; // Per mantenere le descrizioni degli scenari

  constructor(private apiService: ApiService, private router: Router) {}

  ngOnInit(): void {
    this.loadStorie();
  }

  loadStorie(): void {
    this.apiService.getAllStorieTitoli().subscribe(
      (storie: any[]) => {
        this.storie = storie;
        console.log('Storie caricate:', this.storie);
      },
      error => console.error('Errore nel caricamento delle storie', error)
    );
  }

  selectStoria(id: number): void {
    this.apiService.getStoriaById(id).subscribe(storia => {
      this.selectedStoria = storia;
      this.scenarioDescriptions = storia.scenari.map((scenario: any) => scenario.descrizione); // Aggiungi : any
    });
  }

  updateStoria(): void {
    if (this.selectedStoria) {
      const updatedStoria = {
        ...this.selectedStoria,
        scenari: this.selectedStoria.scenari.map((scenario: any, index: number) => ({ // Aggiungi : any
          ...scenario,
          descrizione: this.scenarioDescriptions[index] // Aggiorna la descrizione
        }))
      };

      this.apiService.updateStoria(this.selectedStoria.id, updatedStoria).subscribe(
        () => {
          console.log('Storia aggiornata con successo!');
          this.router.navigate(['/selezione-storia']); // Reindirizza dopo l'aggiornamento
        },
        error => console.error('Errore nell\'aggiornamento della storia', error)
      );
    }
  }
}
