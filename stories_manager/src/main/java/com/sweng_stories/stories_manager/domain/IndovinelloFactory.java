// IndovinelloFactory.java
package com.sweng_stories.stories_manager.domain;

public class IndovinelloFactory {

    public static Indovinello createIndovinello(String tipo, Long id, String descrizione, String domanda, Object rispostaCorretta, Long scenarioId) {
        if ("testuale".equals(tipo)) {
            return new IndovinelloTestuale(id, descrizione, domanda, (String) rispostaCorretta, scenarioId);
        } else if ("numerico".equals(tipo)) {
            return new IndovinelloNumerico(id, descrizione, domanda, (Integer) rispostaCorretta, scenarioId);
        } else {
            throw new IllegalArgumentException("Tipo non riconosciuto: " + tipo);
        }
    }
}
