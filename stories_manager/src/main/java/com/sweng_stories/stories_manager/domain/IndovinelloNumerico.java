// IndovinelloNumerico.java
package com.sweng_stories.stories_manager.domain;

public class IndovinelloNumerico extends Indovinello {
    private Integer rispostaCorretta;

    public IndovinelloNumerico(Long id, String descrizione, Integer rispostaCorretta) {
        super(id, descrizione);
        this.rispostaCorretta = rispostaCorretta;
    }

    public Integer getRispostaCorretta() {
        return rispostaCorretta;
    }

    public void setRispostaCorretta(Integer rispostaCorretta) {
        this.rispostaCorretta = rispostaCorretta;
    }

    @Override
    public boolean verificaRisultato(Object risposta) {
        if (risposta instanceof Integer) {
            return risposta.equals(this.rispostaCorretta);
        }
        return false;
    }
}
