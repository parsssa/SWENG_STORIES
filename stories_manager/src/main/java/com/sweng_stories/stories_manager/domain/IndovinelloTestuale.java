package com.sweng_stories.stories_manager.domain;

public class IndovinelloTestuale extends Indovinello {
    private String rispostaCorretta;

    public IndovinelloTestuale() {}

    public IndovinelloTestuale(Long id, String descrizione, String domanda, String rispostaCorretta, Long scenarioId) {
        super(id, descrizione, domanda, rispostaCorretta, scenarioId);
        this.rispostaCorretta = rispostaCorretta;
    }

    @Override
    public boolean verificaRisultato(Object risposta) {
        if (risposta instanceof String) {
            return rispostaCorretta.equals(risposta);
        }
        return false;
    }

    @Override
    public String getTipo() {
        return "testuale";
    }

    @Override
    public String getRispostaCorretta() {
        return rispostaCorretta;
    }

    @Override
    public void setRispostaCorretta(Object rispostaCorretta) {
        if (rispostaCorretta instanceof String) {
            this.rispostaCorretta = (String) rispostaCorretta;
        }
    }
}
