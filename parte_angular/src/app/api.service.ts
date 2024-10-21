import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class ApiService {
  private baseUrl = 'http://localhost:8080/api';  // Cambia con il tuo endpoint

  constructor(private http: HttpClient) { }

  // Metodi per interagire con le storie
  getAllStorie(): Observable<any> {
    return this.http.get(`${this.baseUrl}/storie`).pipe(
      catchError(this.handleError('getAllStorie'))
    );
  }

  getAllStorieTitoli(): Observable<any[]> {
    return this.http.get<any[]>(`${this.baseUrl}/storie/titoli`).pipe(
      catchError(this.handleError('getAllStorieTitoli'))
    );
  }

  getStoriaById(id: number): Observable<any> {
    return this.http.get<any>(`${this.baseUrl}/storie/${id}`).pipe(
      catchError(this.handleError('getStoriaById'))
    );
  }

 createStoria(storia: any): Observable<any> {
  return this.http.post(`${this.baseUrl}/storie`, storia, {
    headers: { 'Content-Type': 'application/json' }
  }).pipe(
    catchError(this.handleError('createStoria'))
  );
}
  updateStoria(id: number, storia: any): Observable<any> {
    return this.http.put(`${this.baseUrl}/storie/${id}`, storia).pipe(
      catchError(this.handleError('updateStoria'))
    );
  }

  deleteStoria(id: number): Observable<any> {
    return this.http.delete(`${this.baseUrl}/storie/${id}`).pipe(
      catchError(this.handleError('deleteStoria'))
    );
  }

  // Metodi per interagire con gli utenti
  getAllUtenti(): Observable<any> {
    return this.http.get(`${this.baseUrl}/utenti`).pipe(
      catchError(this.handleError('getAllUtenti'))
    );
  }

  loginUtente(credentials: { username: string; password: string; }): Observable<any> {
    return this.http.post(`${this.baseUrl}/login`, credentials).pipe(
      catchError(this.handleError('loginUtente'))
    );
  }

  getUtenteByUsername(username: string): Observable<any> {
    return this.http.get(`${this.baseUrl}/utenti/${username}`).pipe(
      catchError(this.handleError('getUtenteByUsername'))
    );
  }

  createUtente(utente: any): Observable<any> {
    return this.http.post(`${this.baseUrl}/utenti`, utente).pipe(
      catchError(this.handleError('createUtente'))
    );
  }

  updateUtente(username: string, utente: any): Observable<any> {
    return this.http.put(`${this.baseUrl}/utenti/${username}`, utente).pipe(
      catchError(this.handleError('updateUtente'))
    );
  }

  deleteUtente(username: string): Observable<any> {
    return this.http.delete(`${this.baseUrl}/utenti/${username}`).pipe(
      catchError(this.handleError('deleteUtente'))
    );
  }

  // Metodi per interagire con le decisioni delle storie
  makeDecision(storyId: number, decision: any): Observable<any> {
    return this.http.post(`${this.baseUrl}/storie/${storyId}/decisions`, decision).pipe(
      catchError(this.handleError('makeDecision'))
    );
  }

  // Gestione degli errori
  private handleError(operation = 'operation') {
    return (error: any): Observable<never> => {
      console.error(`${operation} failed: ${error.message}`);
      return throwError(() => new Error(error));
    };
  }
}
