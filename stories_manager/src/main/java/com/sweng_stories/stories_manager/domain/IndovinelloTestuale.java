// IndovinelloTestuale.java
package com.sweng_stories.stories_manager.domain;

public class IndovinelloTestuale extends Indovinello {
    private String rispostaCorretta;

    // // Default constructor This is necessary because MongoDB code needs to instantiate these objects without providing any parameters initially.
    // public IndovinelloTestuale() {
    //     super();
    // }

    public IndovinelloTestuale(Long id, String descrizione, String rispostaCorretta) {
        super(id, descrizione);
        this.rispostaCorretta = rispostaCorretta;
    }

    public String getRispostaCorretta() {
        return rispostaCorretta;
    }

    public void setRispostaCorretta(String rispostaCorretta) {
        this.rispostaCorretta = rispostaCorretta;
    }

    @Override
    public boolean verificaRisultato(Object risposta) {
        if (risposta instanceof String) {
            return risposta.equals(this.rispostaCorretta);
        }
        return false;
    }
}
