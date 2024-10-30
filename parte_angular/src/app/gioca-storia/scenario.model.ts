// scenario.model.ts (o il file dove definisci il modello)
export interface Indovinello {
  idScenario: number;
  idScenarioRispGiusta: number;       // ID dello scenario per la risposta corretta
  testoIndovinello: string;           // Testo dell'indovinello
  risposta: string;                   // Risposta corretta
  rispostaSbagliata: string;          // Risposta errata
  idScenarioRispSbagliata: number;    // ID dello scenario per la risposta errata
}


export interface Oggetto {
  id: number;
  nome: string;
  descrizione: string;
}

export interface Alternativa {
  testoAlternativa: string;
  type: string; // "with-items", "without-items", "indovinello"
  idScenarioSuccessivo: number;  // ID dello scenario successivo associato a questa alternativa
}

export interface Scenario {
  idScenario: number;
  descrizione: string;
  indovinelli: Indovinello[];
  oggetti: Oggetto[];
  alternative: Alternativa[];  // Lista delle alternative per questo scenario
}

export interface Storia {
  id: number;
  titolo: string;
  descrizione: string;
  inizio: Scenario; // Scenario iniziale della storia
  finali?: Scenario[]; // Eventuali scenari finali della storia
  indovinello?: Indovinello;
  inventario?: { oggetti: Oggetto[] };
}