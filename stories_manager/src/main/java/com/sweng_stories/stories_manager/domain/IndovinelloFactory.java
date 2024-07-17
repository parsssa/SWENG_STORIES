// IndovinelloFactory.java
package com.sweng_stories.stories_manager.domain;

public class IndovinelloFactory {

    public static Indovinello createIndovinello(String tipo, Long id, String descrizione, Object rispostaCorretta) {
        if ("testuale".equals(tipo)) {
            return new IndovinelloTestuale(id, descrizione, (String) rispostaCorretta);
        } else if ("numerico".equals(tipo)) {
            return new IndovinelloNumerico(id, descrizione, (Integer) rispostaCorretta);
        } else {
            throw new IllegalArgumentException("Tipo non riconosciuto: " + tipo);
        }
    }
}