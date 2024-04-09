// Storia.java
package com.sweng_stories.stories_manager.domain;

import java.util.ArrayList;
import java.util.List;

public class Storia {
    private Long id;
    private String titolo;
    private String descrizione;
    private Scenario inizio;
    private String contenuto;
    private List<Scenario> finali;
    private List<Scenario> scenari;

    public Storia() {
        this.finali = new ArrayList<>();
        this.scenari = new ArrayList<>();
    }
    
    // Costruttore con parametri
    public Storia(Long id, String titolo, String descrizione, Scenario inizio) {
        this.id = id;
        this.titolo = titolo;
        this.descrizione = descrizione;
        this.inizio = inizio;
        this.finali = new ArrayList<>();
        this.scenari = new ArrayList<>();
        this.scenari.add(inizio); // Aggiungi lo scenario iniziale alla lista degli scenari
    }
    
    // Metodo per aggiungere uno scenario alla lista degli scenari della storia
    public void aggiungiScenario(Scenario scenario) {
        this.scenari.add(scenario);
    }

    // Metodo per aggiungere uno scenario finale alla lista dei finali della storia
    public void aggiungiFinale(Scenario finale) {
        this.finali.add(finale);
    }


    // Getter e setter per tutte le proprietà
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public Scenario getInizio() {
        return inizio;
    }

    public void setInizio(Scenario inizio) {
        this.inizio = inizio;
    }

    public List<Scenario> getFinali() {
        return finali;
    }

    public void setFinali(List<Scenario> finali) {
        this.finali = finali;
    }

    public List<Scenario> getScenari() {
        return scenari;
    }

    public void setScenari(List<Scenario> scenari) {
        this.scenari = scenari;
    }

    public String getContenuto(){
        return contenuto;
    }

    public void setContenuto(String contenutoInserimento){
        contenuto= contenutoInserimento;
    }
}
