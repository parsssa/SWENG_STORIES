import { Component, OnInit } from '@angular/core';
import { ApiService } from '../api.service'; // Assicurati di avere il servizio corretto importato
import { Router } from '@angular/router';

@Component({
  selector: 'app-selezione-storia',
  templateUrl: './selezione-storia.component.html',
  styleUrls: ['./selezione-storia.component.scss']
})
export class SelezioneStoriaComponent implements OnInit {
  storie: any[] = []; // Cambia 'any' con un tipo più specifico se necessario

  constructor(private apiService: ApiService, private router: Router) { }

  ngOnInit(): void {
    this.loadStorie();
  }

  loadStorie(): void {
    this.apiService.getAllStorieTitoli().subscribe(
      (storie: any[]) => this.storie = storie,
      error => console.error('Errore nel caricamento delle storie', error)
    );
  }

  selectStoria(id: number): void {
    this.router.navigate(['/giocaStoria', id]);
  }
}
