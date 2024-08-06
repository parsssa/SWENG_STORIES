// Inventario.java
package com.sweng_stories.stories_manager.domain;

import java.util.List;

public class Inventario {
    private Long id;
    private List<Oggetto> oggetti;

    // Costruttori
    public Inventario() {}

    public Inventario(Long id, List<Oggetto> oggetti) {
        this.id = id;
        this.oggetti = oggetti;
    }

    // Getter e setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Oggetto> getOggetti() {
        return oggetti;
    }

    public void setOggetti(List<Oggetto> oggetti) {
        this.oggetti = oggetti;
    }
}
