package com.sweng_stories.stories_manager.domain;

public class IndovinelloTestuale extends Indovinello {
    public IndovinelloTestuale(Long id, String descrizione) {
        super(id, descrizione);
        //TODO Auto-generated constructor stub
    }

    private String rispostaCorretta;

    // Costruttori, getter e setter

    @Override
    public boolean verificaRisultato(Object risposta) {
        return risposta.toString().equalsIgnoreCase(rispostaCorretta);
    }
}