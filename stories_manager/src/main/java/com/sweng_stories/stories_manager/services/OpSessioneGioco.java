package com.sweng_stories.stories_manager.services;


import com.sweng_stories.stories_manager.domain.Inventario;
import com.sweng_stories.stories_manager.domain.Scenario;
import com.sweng_stories.stories_manager.domain.SessioneGioco;

import java.util.ArrayList;

public interface OpSessioneGioco {

    public Scenario elaboraIndovinello(int idScenario, String risposta, int idSessione);
    public Scenario elaboraAlternativa(int idScenario, int idScelta,
                                       int idSessione);
    public Inventario raccogliOggetto(int idSessione, String oggetto);

    public SessioneGioco creaSessione(SessioneGioco sessione);
    public boolean eliminaSessione(int idSessione);
    public ArrayList<SessioneGioco> getSessioniUtente(String username);
    public SessioneGioco getSessioneConID(int idSessione);
}
