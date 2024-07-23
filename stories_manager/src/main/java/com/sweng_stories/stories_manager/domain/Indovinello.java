package com.sweng_stories.stories_manager.domain;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "tipo")
@JsonSubTypes({
    @JsonSubTypes.Type(value = IndovinelloNumerico.class, name = "numerico"),
    @JsonSubTypes.Type(value = IndovinelloTestuale.class, name = "testuale")
})
public abstract class Indovinello {
    private Long id;
    private String descrizione;
    private String domanda;
    private Object rispostaCorretta; // Oggetto generico per la risposta corretta
    private Long scenarioId;

    public Indovinello() {}

    public Indovinello(Long id, String descrizione, String domanda, Object rispostaCorretta, Long scenarioId) {
        this.id = id;
        this.descrizione = descrizione;
        this.domanda = domanda;
        this.rispostaCorretta = rispostaCorretta;
        this.scenarioId = scenarioId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public Long getScenarioId() {
        return scenarioId;
    }

    public void setScenarioId(Long scenarioId) {
        this.scenarioId = scenarioId;
    }

    public abstract boolean verificaRisultato(Object risposta);
    public abstract String getTipo();
}
