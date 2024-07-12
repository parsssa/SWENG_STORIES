import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class StoryService {
  private apiUrl = 'http://localhost:4200/api/stories';  // Cambia con il tuo endpoint

  constructor(private http: HttpClient) { }

  getStory(id: number): Observable<any> {
    return this.http.get(`${this.apiUrl}/${id}`);
  }

  makeDecision(storyId: number, decision: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/${storyId}/decisions`, decision);
  }
}
