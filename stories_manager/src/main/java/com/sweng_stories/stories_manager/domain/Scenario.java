// Scenario.java
package com.sweng_stories.stories_manager.domain;

import java.util.List;

public class Scenario {
    private Long id;
    private String descrizione;
    private List<Indovinello> indovinelli;
    private List<Oggetto> oggetti;

    public Scenario() {
    }

    public Scenario(Long id, String descrizione, List<Indovinello> indovinelli, List<Oggetto> oggetti) {
        this.id = id;
        this.descrizione = descrizione;
        this.indovinelli = indovinelli;
        this.oggetti = oggetti;
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

    public List<Indovinello> getIndovinelli() {
        return indovinelli;
    }

    public void setIndovinelli(List<Indovinello> indovinelli) {
        this.indovinelli = indovinelli;
    }

    public List<Oggetto> getOggetti() {
        return oggetti;
    }

    public void setOggetti(List<Oggetto> oggetti) {
        this.oggetti = oggetti;
    }
}