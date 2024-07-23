package com.sweng_stories.stories_manager.domain;

public class IndovinelloNumerico extends Indovinello {
    private Integer rispostaCorretta;

    // Costruttore vuoto
    public IndovinelloNumerico() {}

    public IndovinelloNumerico(Long id, String descrizione, String domanda, Integer rispostaCorretta, Long scenarioId) {
        super(id, descrizione, domanda, rispostaCorretta, scenarioId);
    }

    @Override
    public boolean verificaRisultato(Object risposta) {
        if (risposta instanceof Integer) {
            return rispostaCorretta.equals(risposta);
        }
        return false;
    }

    @Override
    public String getTipo() {
        return "numerico";
    }

    @Override
    public Integer getRispostaCorretta() {
        return rispostaCorretta;
    }

    @Override
    public void setRispostaCorretta(Object rispostaCorretta) {
        if (rispostaCorretta instanceof Integer) {
            this.rispostaCorretta = (Integer) rispostaCorretta;
        }
    }

    @Override
    public String getDomanda() {
        return super.getDomanda();
    }

    @Override
    public void setDomanda(String domanda) {
        super.setDomanda(domanda);
    }

    @Override
    public Long getScenarioId() {
        return super.getScenarioId();
    }

    @Override
    public void setScenarioId(Long scenarioId) {
        super.setScenarioId(scenarioId);
    }
}
