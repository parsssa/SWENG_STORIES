package com.sweng_stories.stories_manager.domain;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "tipo")
@JsonSubTypes({
    @JsonSubTypes.Type(value = IndovinelloNumerico.class, name = "numerico"),
    @JsonSubTypes.Type(value = IndovinelloTestuale.class, name = "testuale")
})
public abstract class Indovinello {
    private int id;
    private String descrizione;
    private String domanda;
    private Object rispostaCorretta; // Oggetto generico per la risposta corretta
    private int scenarioId;  // Scenario successivo in caso di risposta corretta

    public Indovinello() {}

    public Indovinello(int id, String descrizione, String domanda, Object rispostaCorretta, int scenarioId) {
        this.id = id;
        this.descrizione = descrizione;
        this.domanda = domanda;
        this.rispostaCorretta = rispostaCorretta;
        this.scenarioId = scenarioId;
    }

    // Getter e setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public String getDomanda() {
        return domanda;
    }

    public void setDomanda(String domanda) {
        this.domanda = domanda;
    }

    public Object getRispostaCorretta() {
        return rispostaCorretta;
    }

    public void setRispostaCorretta(Object rispostaCorretta) {
        this.rispostaCorretta = rispostaCorretta;
    }

    public int getScenarioId() {
        return scenarioId;
    }

    public void setScenarioId(int scenarioId) {
        this.scenarioId = scenarioId;
    }

    // Metodo astratto per verificare la risposta data
    public abstract boolean verificaRisultato(Object risposta);

    // Metodo astratto per ottenere il tipo di indovinello
    public abstract String getTipo();
}
