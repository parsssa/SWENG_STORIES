import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class ApiService {
  private baseUrl = 'http://localhost:8080/api';  // Cambia con il tuo endpoint

  private httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  };

  constructor(private http: HttpClient) { }

  // Metodi per interagire con le storie
  getAllStorie(): Observable<any> {
    return this.http.get(`${this.baseUrl}/storie/storie`, this.httpOptions).pipe(
      catchError(this.handleError('getAllStorie'))
    );
  }

  getStoriaById(id: number): Observable<any> {
    return this.http.get<any>(`${this.baseUrl}/storie/storie/${id}`, this.httpOptions).pipe(
      catchError(this.handleError('getStoriaById'))
    );
  }

  getStoriaConUsername(username: string): Observable<any[]> {
    return this.http.get<any[]>(`${this.baseUrl}/storie/utente/${username}`, this.httpOptions).pipe(
      catchError(this.handleError('getStoriaConUsername'))
    );
  }

  inserisciStoria(storia: any): Observable<any> {
    console.log("DETTAGLI INVIO POWER");
    console.log(storia); // Rimuovi JSON.stringify qui
    const headers = new HttpHeaders({
        'Content-Type': 'application/json'
    });
    return this.http.post<any>(`${this.baseUrl}/storie`, storia, { headers }).pipe(
        catchError(this.handleError('inserisciStoria'))
    );
}

  

  updateStoria(idStoria: number, idScenario: number, nuovoTesto: string): Observable<any> {
    return this.http.put(`${this.baseUrl}/storie/storie/${idStoria}/scenari/${idScenario}`, {}, {
      params: { nuovoTesto },
      ...this.httpOptions
    }).pipe(
      catchError(this.handleError('updateStoria'))
    );
  }

  getScenario(idStoria: number, idScenario: number): Observable<any> {
    return this.http.get<any>(`${this.baseUrl}/storie/storie/${idStoria}/scenari/${idScenario}`, this.httpOptions).pipe(
      catchError(this.handleError('getScenario'))
    );
  }

  inserisciScenario(idStoria: number, scenario: any): Observable<any> {
    return this.http.post(`${this.baseUrl}/storie/${idStoria}/scenari`, scenario, this.httpOptions).pipe(
      catchError(this.handleError('inserisciScenario'))
    );
  }

  getScenariStoria(idStoria: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.baseUrl}/storie/storie/${idStoria}/scenari`, this.httpOptions).pipe(
      catchError(this.handleError('getScenariStoria'))
    );
  }

  // Metodi per interagire con le sessioni di gioco
  elaboraIndovinello(idPartita: number, idScenario: number, risposta: string): Observable<any> {
    return this.http.put(`${this.baseUrl}/sessioni/SessioneGioco/${idPartita}/scenari/${idScenario}/indovinello`, {}, {
      params: { risposta },
      ...this.httpOptions
    }).pipe(
      catchError(this.handleError('elaboraIndovinello'))
    );
  }

  elaboraAlternativa(idPartita: number, idScenario: number, idScelta: number): Observable<any> {
    return this.http.put(`${this.baseUrl}/sessioni/SessioneGioco/${idPartita}/scenari/${idScenario}/alternativa`, {}, {
      params: { idScelta: idScelta.toString() },
      ...this.httpOptions
    }).pipe(
      catchError(this.handleError('elaboraAlternativa'))
    );
  }

  raccogliOggetto(idPartita: number, oggetto: string): Observable<any> {
    return this.http.post(`${this.baseUrl}/sessioni/SessioneGioco/${idPartita}/inventario`, {}, {
      params: { oggetto },
      ...this.httpOptions
    }).pipe(
      catchError(this.handleError('raccogliOggetto'))
    );
  }

  creaSessione(partita: any): Observable<any> {
    return this.http.post<any>(`${this.baseUrl}/sessioni/SessioneGioco/`, partita, this.httpOptions).pipe(
      catchError(this.handleError('creaSessione'))
    );
  }

  eliminaSessione(idPartita: number): Observable<any> {
    return this.http.delete(`${this.baseUrl}/sessioni/SessioneGioco/${idPartita}`, this.httpOptions).pipe(
      catchError(this.handleError('eliminaSessione'))
    );
  }

  getSessioniUtente(username: string): Observable<any[]> {
    return this.http.get<any[]>(`${this.baseUrl}/sessioni/SessioneGioco/utente/${username}`, this.httpOptions).pipe(
      catchError(this.handleError('getSessioniUtente'))
    );
  }

  getSessioneConID(idPartita: number): Observable<any> {
    return this.http.get<any>(`${this.baseUrl}/sessioni/SessioneGioco/${idPartita}`, this.httpOptions).pipe(
      catchError(this.handleError('getSessioneConID'))
    );
  }

  // Metodi per interagire con l'autenticazione
  registraUtente(username: string, password: string): Observable<any> {
    return this.http.post(`${this.baseUrl}/auth/AuthController/register`, {}, {
      params: { username, password },
      ...this.httpOptions
    }).pipe(
      catchError(this.handleError('registraUtente'))
    );
  }

  loginUtente(formData: { username: string; password: string }): Observable<any> {
    return this.http.post(`${this.baseUrl}/auth/AuthController/login`, {}, {
      params: { username: formData.username, password: formData.password },
      ...this.httpOptions
    }).pipe(
      catchError(this.handleError('loginUtente'))
    );
}


  getUtente(username: string): Observable<any> {
    return this.http.get(`${this.baseUrl}/auth/AuthController/user/${username}`, this.httpOptions).pipe(
      catchError(this.handleError('getUtente'))
    );
  }

  // Gestione degli errori
  private handleError(operation = 'operation') {
    return (error: any): Observable<never> => {
      console.error(`${operation} failed: ${error.message}`);
      return throwError(() => new Error(`Operazione ${operation} fallita: ${error.message}`));
    };
  }
}
