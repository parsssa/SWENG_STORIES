import { Component, OnInit } from '@angular/core';
import { ApiService } from '../api.service';
import { Router, NavigationExtras } from '@angular/router';

@Component({
  selector: 'app-selezione-storia',
  templateUrl: './selezione-storia.component.html',
  styleUrls: ['./selezione-storia.component.scss']
})
export class SelezioneStoriaComponent implements OnInit {
  storie: any[] = [];

  constructor(private apiService: ApiService, private router: Router) {}

  ngOnInit(): void {
    this.loadStorie();
  }

  loadStorie(): void {
    this.apiService.getAllStorie().subscribe(
      (storie: any[]) => {
        this.storie = storie;
        console.log('Storie caricate:', this.storie);
      },
      error => console.error('Errore nel caricamento delle storie', error)
    );
  }

  selectStoria(id: number): void {
    const storiaTrovata = this.storie.find(storia => storia.id === id);
    if (storiaTrovata) {
      this.apiService.creaSessione({
        idStoria: id,
        username: 'utenteCorrente',
        idScenarioCorrente: 0,
        inventario: { oggetti: [] }
      }).subscribe(newSessione => {
        if (newSessione) {
          console.log(storiaTrovata.inizio) //////
          const navigationExtras: NavigationExtras = {
            state: {
              selectedStoria: storiaTrovata,
              sessioneId: newSessione.idSessione,
              inventory: newSessione.inventario.oggetti
            }
          };
          this.router.navigate(['/giocaStoria', id], navigationExtras);
        } else {
          console.error('Errore nella creazione della nuova sessione');
        }
      });
    }
  }

  editStoria(id: number): void {
    this.router.navigate(['/modifica-storia', id]);
  }
}
