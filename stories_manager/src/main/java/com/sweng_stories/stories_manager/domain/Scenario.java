package com.sweng_stories.stories_manager.domain;

import java.util.ArrayList;
import java.util.List;

public class Scenario {

    private int idStoria;

    private int idScenario;

    private String testoScenario;
    private String oggetto;

    private List<Alternativa> alternative;
    private Indovinello indovinello;

    public Scenario() {
    }

    public void setIdScenario(int idScenario) {
        this.idScenario = idScenario;
    }

    public void setAlternative(List<Alternativa> alternative) {
        this.alternative = alternative;
    }

    public Scenario(int idStoria, int idScenario, String testoScenario, String oggetto, List<Alternativa> alternative, Indovinello indovinello) {
        this.idStoria = idStoria;
        this.idScenario = idScenario;
        this.testoScenario = testoScenario;
        this.oggetto = oggetto;
        this.alternative = alternative;
        this.indovinello = indovinello;
    }

    public int getIdScenario() {
        return idScenario;
    }

    public void setAlternative(ArrayList<Alternativa> alternative) {
        this.alternative = alternative;
    }

    public Indovinello getIndovinello() {
        return indovinello;
    }

    public void setIndovinello(Indovinello indovinello) {
        this.indovinello = indovinello;
    }

    public void setIdStoria(int idStoria) {
        this.idStoria = idStoria;
    }

    public String getOggetto() {
        return oggetto;
    }

    public void setOggetto(String oggetto) {
        this.oggetto = oggetto;
    }

    public int getIdStoria() {
        return idStoria;
    }

    public String getTestoScenario() {
        return testoScenario;
    }

    public List<Alternativa> getAlternative() {
        return alternative;
    }

    public void setTestoScenario(String testoScenario) {
        this.testoScenario = testoScenario;
    }
    public void aggiungiAlternativa(Alternativa alternativa){
        alternative.add(alternativa);
    }

}
