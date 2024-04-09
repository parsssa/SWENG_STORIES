// SessioneGioco.java
package com.sweng_stories.stories_manager.domain;

public class SessioneGioco {
    private Utente utente;
    private Storia storiaCorrente;
    private Inventario inventario;

    public SessioneGioco(Utente utente, Storia storiaCorrente, Inventario inventario) {
        this.utente = utente;
        this.storiaCorrente = storiaCorrente;
        this.inventario = inventario;
    }

    public Utente getUtente() {
        return utente;
    }

    public void setUtente(Utente utente) {
        this.utente = utente;
    }

    public Storia getStoriaCorrente() {
        return storiaCorrente;
    }

    public void setStoriaCorrente(Storia storiaCorrente) {
        this.storiaCorrente = storiaCorrente;
    }

    public Inventario getInventario() {
        return inventario;
    }

    public void setInventario(Inventario inventario) {
        this.inventario = inventario;
    }

    // Altri metodi per gestire lo stato del gioco e l'inventario dell'utente
}
