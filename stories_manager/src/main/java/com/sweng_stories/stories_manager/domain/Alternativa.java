package com.sweng_stories.stories_manager.domain;

public class Alternativa {

    private int idScenario;
    private int idScenarioSuccessivo;
    private String testoAlternativa;
    private String oggettoRichiesto;

    public Alternativa(int idScenario, int idScenarioSuccessivo, String testoAlternativa, String oggettoRichiesto) {
        this.idScenario = idScenario;
        this.idScenarioSuccessivo = idScenarioSuccessivo;
        this.testoAlternativa = testoAlternativa;
        this.oggettoRichiesto = oggettoRichiesto;
    }

    public Alternativa() {
    }

    public void setIdScenario(int idScenario) {
        this.idScenario = idScenario;
    }

    public void setIdScenarioSuccessivo(int idScenarioSuccessivo) {
        this.idScenarioSuccessivo = idScenarioSuccessivo;
    }

    public void setTestoAlternativa(String testoAlternativa) {
        this.testoAlternativa = testoAlternativa;
    }

    public void setOggettoRichiesto(String oggettoRichiesto) {
        this.oggettoRichiesto = oggettoRichiesto;
    }

    public String getTestoAlternativa() {
        return testoAlternativa;
    }

    public String getOggettoRichiesto() {
        return oggettoRichiesto;
    }

    public int getIdScenario() {
        return idScenario;
    }

    public int getIdScenarioSuccessivo() {
        return idScenarioSuccessivo;
    }

}
