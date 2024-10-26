package com.sweng_stories.stories_manager.domain;

import java.util.ArrayList;
import java.util.List;

public class Inventario {

    private List<String> oggetti;

    public Inventario(List<String> oggetti){
        this.oggetti = oggetti;
    }

    public Inventario() {
    }

    public boolean raccogliOggetto(String oggetto){

        if(!oggetti.contains(oggetto)){
          oggetti.add(oggetto);
          return true;
        }

        return false;
    }

    public boolean eliminaOggetto(String oggetto){
        return oggetti.remove(oggetto);
    }

    public List<String> getOggetti(){
        return oggetti;
    }
}
