package com.sweng_stories.stories_manager.domain;

public class Storia {
    private String titolo;

    private Scenario inizio;

    private int id;

    private String username;

    public Storia() {
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Storia(String titolo, String username) {
        this.titolo = titolo;
        this.username = username;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getTitolo() {
        return titolo;
    }

    public Scenario getInizio() {
        return inizio;
    }

    public void setInizio(Scenario inizio) {
        this.inizio = inizio;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }
}
