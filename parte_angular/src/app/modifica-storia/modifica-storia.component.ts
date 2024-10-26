import { Component, OnInit } from '@angular/core';
import { ApiService } from '../api.service';
import { Router, ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-modifica-storia',
  templateUrl: './modifica-storia.component.html',
  styleUrls: ['./modifica-storia.component.scss']
})
export class ModificaStoriaComponent implements OnInit {
  selectedStoria: any = null;
  scenarioDescriptions: string[] = []; // Per mantenere le descrizioni degli scenari

  constructor(private apiService: ApiService, private router: Router, private route: ActivatedRoute) {}

  ngOnInit(): void {
    // Ottieni l'id della storia dalla rotta
    const storiaId = Number(this.route.snapshot.paramMap.get('id'));
    this.loadStoria(storiaId); // Carica solo la storia selezionata
  }

  loadStoria(id: number): void {
    this.apiService.getStoriaById(id).subscribe(storia => {
      this.selectedStoria = storia;
      this.scenarioDescriptions = storia.scenari.map((scenario: any) => scenario.descrizione); // Aggiungi : any
    }, error => {
      console.error('Errore nel caricamento della storia', error);
      this.router.navigate(['/selezione-storia']); // Reindirizza se c'è un errore
    });
  }

  updateStoria(): void {
    if (this.selectedStoria) {
      this.selectedStoria.scenari.forEach((scenario: any, index: number) => {
        const nuovoTesto = this.scenarioDescriptions[index]; // Testo aggiornato dello scenario
        const idScenario = scenario.id; // Id dello scenario
  
        this.apiService.updateStoria(this.selectedStoria.id, idScenario, nuovoTesto).subscribe(
          () => {
            console.log(`Scenario ${idScenario} aggiornato con successo!`);
            // Se vuoi reindirizzare dopo l'ultimo aggiornamento, fallo solo all'ultimo ciclo
            if (index === this.selectedStoria.scenari.length - 1) {
              this.router.navigate(['/selezione-storia']);
            }
          },
          error => console.error(`Errore nell'aggiornamento dello scenario ${idScenario}`, error)
        );
      });
    }
  }
}
