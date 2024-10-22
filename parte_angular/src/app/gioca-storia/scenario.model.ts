export interface Indovinello {
  id: number;
  descrizione: string;
  domanda: string;
  rispostaCorretta: any;
  scenarioId: number;  // ID dello scenario successivo
  tipo: string;
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
