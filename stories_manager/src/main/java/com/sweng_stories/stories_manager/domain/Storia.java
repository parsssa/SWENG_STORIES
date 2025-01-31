// Storia.java
package com.sweng_stories.stories_manager.domain;

import java.util.ArrayList;
import java.util.List;

public class Storia {
    private Long id;
    private String titolo;
    private String descrizione;
    private Scenario inizio;
    private List<Scenario> finali;
    private List<Scenario> scenari;
    private Inventario inventario;

    public Storia() {
        this.id = 0L; // ID predefinito che può essere sovrascritto dal controller
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
        // Ciclo per stampare ogni scenario
        for (Scenario scenario : scenari) {
            System.out.println(scenario.toString());  // Stampa il risultato di toString() di ogni scenario
        }
    
        // Restituisce la lista di scenari come previsto
        return scenari;
    }
    

    public void setScenari(List<Scenario> scenari) {
        this.scenari = scenari;
    }


    public void setId(Long id) {
        this.id = id;
    }

    public Inventario getInventario() {
        return inventario;
    }

    public void setInventario(Inventario inventario) {
        this.inventario = inventario;
    }

    // Metodi aggiuntivi per supportare la nuova struttura dei dati
    public void addFinale(Scenario finale) {
        this.finali.add(finale);
    }

    public void addScenario(Scenario scenario) {
        this.scenari.add(scenario);
    }

    public void setInizio(String descrizione) {
        this.inizio = new Scenario();
        this.inizio.setDescrizione(descrizione);
    }

    public void setFinaliFromDescriptions(List<String> descriptions) {
        this.finali = new ArrayList<>();
        for (String description : descriptions) {
            Scenario scenario = new Scenario();
            scenario.setDescrizione(description);
            this.finali.add(scenario);
        }
    }
/*
 * 
 * 
 * 
    public void setScenariFromAlternatives(List<Alternative> alternatives) {
        this.scenari = new ArrayList<>();
        for (Alternative alt : alternatives) {
            Scenario scenario = new Scenario();
            scenario.setDescrizione(alt.getText());

            // Aggiungi oggetti se presenti
            List<Oggetto> oggetti = new ArrayList<>();
            for (String itemName : alt.getItems()) {
                Oggetto oggetto = new Oggetto();
                oggetto.setNome(itemName.trim());
                oggetto.setDescrizione("");  // Set a default description or modify as needed
                oggetti.add(oggetto);
            }
            scenario.setOggetti(oggetti);

            // Non c'è più bisogno di aggiungere `nextScenarioIds`, gestiamo tutto con le alternative
            this.scenari.add(scenario);
        }
    }

 * 
 * 
 * 
 * 
 * 
 * 
 */

 

    public void setInventarioFromItems(String items) {
        this.inventario = new Inventario();
        List<Oggetto> oggetti = new ArrayList<>();
        for (String itemName : items.split(",")) {
            Oggetto oggetto = new Oggetto();
            oggetto.setNome(itemName.trim());
            oggetto.setDescrizione("");  // Set a default description or modify as needed
            oggetti.add(oggetto);
        }
        this.inventario.setOggetti(oggetti);
    }


    @Override
    public String toString() {
        return "Storia [id=" + id + ", titolo=" + titolo + ", descrizione=" + descrizione + ", inizio=" + inizio
                + ", finali=" + finali + ", scenari=" + scenari + ", inventario=" + inventario + "]";
    }
}
