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

export interface Alternative {
  text: string;
  type: string; // "with-items", "without-items", "indovinello"
  nextScenarioId: number;  // ID dello scenario successivo associato a questa alternativa
}

export interface Scenario {
  id: number;
  descrizione: string;
  indovinelli: Indovinello[];
  oggetti: Oggetto[];
  alternatives: Alternative[];  // Lista delle alternative per questo scenario
}

export interface Storia {
  id: number;
  titolo: string;
  descrizione: string;
  inizio: Scenario;
  finali: Scenario[];
  scenari: Scenario[];
  indovinello: Indovinello;
  inventario: { oggetti: Oggetto[] };
}
