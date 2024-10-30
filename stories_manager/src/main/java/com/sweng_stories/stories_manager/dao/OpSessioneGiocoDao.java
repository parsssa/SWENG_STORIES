package com.sweng_stories.stories_manager.dao;

import com.sweng_stories.stories_manager.domain.Scenario;
import com.sweng_stories.stories_manager.domain.SessioneGioco;

import java.util.ArrayList;

public interface OpSessioneGiocoDao {

    public SessioneGioco creaSessione(SessioneGioco sessione);
    public boolean eliminaSessione(int idSessione);
    public ArrayList<SessioneGioco> getSessioniUtente(String username);
    public SessioneGioco getSessioneConID(String idSessione);

    Scenario getScenarioCorrente(int idStoria, int idScenario);

    boolean aggiornaSessione(SessioneGioco sessione);
}
