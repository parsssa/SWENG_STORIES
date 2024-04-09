// Utente.java
package com.sweng_stories.stories_manager.domain;

import java.util.ArrayList;
import java.util.List;

public class Utente {
    private String username;
    private String password;
    private List<Storia> storieScritte;
    private List<Storia> storieGiocate;

    public Utente() {
        this.storieScritte = new ArrayList<>();
        this.storieGiocate = new ArrayList<>();
    }

    // Costruttore con parametri
    public Utente(String username, String password) {
        this.username = username;
        this.password = password;
        this.storieScritte = new ArrayList<>();
        this.storieGiocate = new ArrayList<>();
    }
    
    // Metodo per aggiungere una storia scritta dall'utente
    public void aggiungiStoriaScritta(Storia storia) {
        this.storieScritte.add(storia);
    }

    // Metodo per aggiungere una storia giocata dall'utente
    public void aggiungiStoriaGiocata(Storia storia) {
        this.storieGiocate.add(storia);
    }


    // Getter e setter per tutte le proprietà
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Storia> getStorieScritte() {
        return storieScritte;
    }

    public void setStorieScritte(List<Storia> storieScritte) {
        this.storieScritte = storieScritte;
    }

    public List<Storia> getStorieGiocate() {
        return storieGiocate;
    }

    public void setStorieGiocate(List<Storia> storieGiocate) {
        this.storieGiocate = storieGiocate;
    }

}
