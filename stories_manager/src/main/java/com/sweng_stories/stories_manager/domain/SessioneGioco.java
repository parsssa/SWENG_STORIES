package com.sweng_stories.stories_manager.domain;

public class SessioneGioco {
    private int idStoria;
    private Inventario inventario;
    private String username;
    private int idScenarioCorrente;
    private int idSessione;

    public SessioneGioco() {
    }

    public SessioneGioco(String username, Integer idStoria, Integer idScenarioCorrente, Inventario inventario) {
        this.username = username;
        this.idStoria = idStoria;
        this.idScenarioCorrente = idScenarioCorrente;
        this.inventario = inventario;
    }

    public int getIdStoria() {
        return idStoria;
    }

    public void setIdStoria(int idStoria) {
        this.idStoria = idStoria;
    }

    public void setInventario(Inventario inventario) {
        this.inventario = inventario;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public SessioneGioco(int idStoria, String username, int idScenarioCorrente) {
        this.idStoria = idStoria;
        this.username = username;
        this.idScenarioCorrente = idScenarioCorrente;
    }

    public void setInventario(){
        inventario = new Inventario();
    }

    public Inventario getInventario() {
        return inventario;
    }

    public String getUsername() {
        return username;
    }

    public int getIdScenarioCorrente() {
        return idScenarioCorrente;
    }

    public void setIdScenarioCorrente(int idScenarioCorrente) {
        this.idScenarioCorrente = idScenarioCorrente;
    }

    private boolean aggiungiOggetto(String oggetto){
        return inventario.raccogliOggetto(oggetto);
    }
    public int getIdSessione() {
        return idSessione;
    }

    public void setIdSessione(int idSessione) {
        this.idSessione = idSessione;
    }

}
