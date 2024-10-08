// Scenario.java
package com.sweng_stories.stories_manager.domain;

import java.util.ArrayList;
import java.util.List;

public class Scenario {
    private Long id;
    private String descrizione;
    private List<Indovinello> indovinelli;
    private List<Oggetto> oggetti;
    private List<Long> nextScenarioIds;

    public Scenario() {
        this.indovinelli = new ArrayList<>();
        this.oggetti = new ArrayList<>();
        this.nextScenarioIds = new ArrayList<>();
    }

    public Scenario(Long id, String descrizione, List<Indovinello> indovinelli, List<Oggetto> oggetti, List<Long> nextScenarioIds) {
        this.id = id;
        this.descrizione = descrizione;
        this.indovinelli = (indovinelli != null) ? indovinelli : new ArrayList<>();
        this.oggetti = (oggetti != null) ? oggetti : new ArrayList<>();
        this.nextScenarioIds = (nextScenarioIds != null) ? nextScenarioIds : new ArrayList<>();
    }

    // Getter e setter
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

    public List<Indovinello> getIndovinelli() {
        return indovinelli;
    }

    public void setIndovinelli(List<Indovinello> indovinelli) {
        this.indovinelli = (indovinelli != null) ? indovinelli : new ArrayList<>();
    }

    public List<Oggetto> getOggetti() {
        return oggetti;
    }

    public void setOggetti(List<Oggetto> oggetti) {
        this.oggetti = (oggetti != null) ? oggetti : new ArrayList<>();
    }

    public List<Long> getNextScenarioIds() {
        return nextScenarioIds;
    }

    public void setNextScenarioIds(List<Long> nextScenarioIds) {
        this.nextScenarioIds = (nextScenarioIds != null) ? nextScenarioIds : new ArrayList<>();
    }
}
