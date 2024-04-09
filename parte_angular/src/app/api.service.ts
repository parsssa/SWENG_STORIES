// api.service.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ApiService {
  private baseUrl = 'http://localhost:8080/api';

  constructor(private http: HttpClient) { }

  // Metodi per interagire con le storie
  getAllStorie(): Observable<any> {
    return this.http.get(`${this.baseUrl}/storie`);
  }

  getStoriaById(id: number): Observable<any> {
    return this.http.get(`${this.baseUrl}/storie/${id}`);
  }

  createStoria(storia: any): Observable<any> {
    return this.http.post(`${this.baseUrl}/storie`, storia);
  }

  updateStoria(id: number, storia: any): Observable<any> {
    return this.http.put(`${this.baseUrl}/storie/${id}`, storia);
  }

  deleteStoria(id: number): Observable<any> {
    return this.http.delete(`${this.baseUrl}/storie/${id}`);
  }

  // Metodi per interagire con gli utenti
  getAllUtenti(): Observable<any> {
    return this.http.get(`${this.baseUrl}/utenti`);
  }

  // Aggiungi questo metodo per il login
  loginUtente(credentials: { username: string; password: string; }): Observable<any> {
    return this.http.post(`${this.baseUrl}/login`, credentials);
  }


  getUtenteByUsername(username: string): Observable<any> {
    return this.http.get(`${this.baseUrl}/utenti/${username}`);
  }

  createUtente(utente: any): Observable<any> {
    return this.http.post(`${this.baseUrl}/utenti`, utente);
  }

  updateUtente(username: string, utente: any): Observable<any> {
    return this.http.put(`${this.baseUrl}/utenti/${username}`, utente);
  }

  deleteUtente(username: string): Observable<any> {
    return this.http.delete(`${this.baseUrl}/utenti/${username}`);
  }

  // Aggiungi altri metodi per interagire con le altre entità del backend
}