export interface Oggetto {
    id: number;
    nome: string;
    descrizione: string;
  }
  
  export interface Indovinello {
    tipo: string;
    id: number;
    descrizione: string;
  }
  
  export interface Alternative {
    text: string;
    nextScenarioId: number;
    requiredItem?: string;
  }
  
  export interface Scenario {
    id: number;
    descrizione: string;
    indovinelli: Indovinello[];
    oggetti: Oggetto[];
    alternative: Alternative[];
  }
  