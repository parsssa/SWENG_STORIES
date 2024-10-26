package com.sweng_stories.stories_manager.domain;

public class IndovinelloNumerico extends Indovinello {
    private Integer rispostaCorretta;

    public IndovinelloNumerico() {}

    public IndovinelloNumerico(int id, String descrizione, String domanda, Integer rispostaCorretta, int scenarioId) {
        super(id, descrizione, domanda, rispostaCorretta, scenarioId);
        this.rispostaCorretta = rispostaCorretta;
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
            super.setRispostaCorretta(rispostaCorretta);
        }
    }
}
