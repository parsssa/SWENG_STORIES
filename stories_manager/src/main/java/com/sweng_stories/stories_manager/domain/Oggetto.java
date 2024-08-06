// Oggetto.java
package com.sweng_stories.stories_manager.domain;

public class Oggetto {
    private Long id;
    private String nome;
    private String descrizione;
    private Long scenarioId;  // Aggiungi questo campo

    // Costruttori
    public Oggetto() {
    }

    public Oggetto(Long id, String nome, String descrizione, Long scenarioId) {
        this.id = id;
        this.nome = nome;
        this.descrizione = descrizione;
        this.scenarioId = scenarioId;
    }

    // Getter e setter per id
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    // Getter e setter per nome
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    // Getter e setter per descrizione
    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    // Getter e setter per scenarioId
    public Long getScenarioId() {
        return scenarioId;
    }

    public void setScenarioId(Long scenarioId) {
        this.scenarioId = scenarioId;
    }

    @Override
    public String toString() {
        return "Oggetto{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", descrizione='" + descrizione + '\'' +
                ", scenarioId=" + scenarioId +
                '}';
    }
}
