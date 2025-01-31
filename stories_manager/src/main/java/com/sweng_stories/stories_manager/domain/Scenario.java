package com.sweng_stories.stories_manager.domain;

import java.util.ArrayList;
import java.util.List;

public class Scenario {
    private Long id;
    private String descrizione;
    private List<Indovinello> indovinelli;
    private List<Oggetto> oggetti;
    private List<Alternative> alternatives;

    public Scenario() {
        this.indovinelli = new ArrayList<>();
        this.oggetti = new ArrayList<>();
        this.alternatives = new ArrayList<>();
    }

    public Scenario(Long id, String descrizione, List<Indovinello> indovinelli, List<Oggetto> oggetti, List<Alternative> alternatives) {
        this.id = id;
        this.descrizione = descrizione;
        this.indovinelli = (indovinelli != null) ? indovinelli : new ArrayList<>();
        this.oggetti = (oggetti != null) ? oggetti : new ArrayList<>();
        this.alternatives = (alternatives != null) ? alternatives : new ArrayList<>();
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

    public List<Alternative> getAlternatives() {
        return alternatives;
    }

    public void setAlternatives(List<Alternative> alternatives) {
        this.alternatives = (alternatives != null) ? alternatives : new ArrayList<>();
    }

    // Aggiungi un indovinello alla lista
    public void addIndovinello(Indovinello indovinello) {
        if (indovinello != null) {
            this.indovinelli.add(indovinello);
        }
    }



    
    @Override
    public String toString() {
        return "Scenario [id=" + id + ", descrizione=" + descrizione + ", indovinelli=" + indovinelli + ", oggetti="
                + oggetti + ", alternatives=" + alternatives + "]";
    }

    // Rimuovi un indovinello dalla lista
    public void removeIndovinello(Indovinello indovinello) {
        this.indovinelli.remove(indovinello);
    }
}
