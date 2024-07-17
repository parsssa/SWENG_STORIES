// Oggetto.java
package com.sweng_stories.stories_manager.domain;

public class Oggetto {
    private Long id;
    private String nome;
    private String descrizione;

    // Costruttore vuoto
    public Oggetto() {
    }

    // Costruttore parametrico
    public Oggetto(Long id, String nome, String descrizione) {
        this.id = id;
        this.nome = nome;
        this.descrizione = descrizione;
    }

    // Getter e Setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    // Metodo toString per una rappresentazione testuale
    @Override
    public String toString() {
        return "Oggetto{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", descrizione='" + descrizione + '\'' +
                '}';
    }

    // Override di equals per confrontare oggetti
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Oggetto oggetto = (Oggetto) o;

        if (id != null ? !id.equals(oggetto.id) : oggetto.id != null) return false;
        if (nome != null ? !nome.equals(oggetto.nome) : oggetto.nome != null) return false;
        return descrizione != null ? descrizione.equals(oggetto.descrizione) : oggetto.descrizione == null;
    }

    // Override di hashCode per l'uso con strutture dati basate su hash
    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (nome != null ? nome.hashCode() : 0);
        result = 31 * result + (descrizione != null ? descrizione.hashCode() : 0);
        return result;
    }
}