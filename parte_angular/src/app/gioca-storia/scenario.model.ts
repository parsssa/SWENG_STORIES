// scenario.model.ts
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
  
  export interface Scenario {
    id: number;
    descrizione: string;
    indovinelli: Indovinello[];
    oggetti: Oggetto[];
    nextScenarioIds: number[];  // Lista degli ID degli scenari successivi
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
  