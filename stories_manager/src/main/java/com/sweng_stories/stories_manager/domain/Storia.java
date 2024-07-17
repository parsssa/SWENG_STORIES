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
    private Indovinello indovinello;
    private Inventario inventario;

    public Storia() {
        this.finali = new ArrayList<>();
        this.scenari = new ArrayList<>();
    }

    public Storia(Long id, String titolo, String descrizione, Scenario inizio) {
        this.id = id;
        this.titolo = titolo;
        this.descrizione = descrizione;
        this.inizio = inizio;
        this.finali = new ArrayList<>();
        this.scenari = new ArrayList<>();
        this.scenari.add(inizio);
    }

    public void aggiungiScenario(Scenario scenario) {
        this.scenari.add(scenario);
    }

    public void aggiungiFinale(Scenario finale) {
        this.finali.add(finale);
    }

    // Getter e setter
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

    public String getContenuto() {
        return contenuto;
    }

    public void setContenuto(String contenuto) {
        this.contenuto = contenuto;
    }

    public Indovinello getIndovinello() {
        return indovinello;
    }

    public void setIndovinello(Indovinello indovinello) {
        this.indovinello = indovinello;
    }

    public Inventario getInventario() {
        return inventario;
    }

    public void setInventario(Inventario inventario) {
        this.inventario = inventario;
    }
}
