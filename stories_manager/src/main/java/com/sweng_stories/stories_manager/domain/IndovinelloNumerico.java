package com.sweng_stories.stories_manager.domain;

public class IndovinelloNumerico extends Indovinello {
    public IndovinelloNumerico(Long id, String descrizione) {
        super(id, descrizione);
        //TODO Auto-generated constructor stub
    }

    private int rispostaCorretta;

    // Costruttori, getter e setter

    @Override
    public boolean verificaRisultato(Object risposta) {
        return risposta instanceof Integer && (int) risposta == rispostaCorretta;
    }
}