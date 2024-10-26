package com.sweng_stories.stories_manager.services;

import com.sweng_stories.stories_manager.domain.Scenario;
import com.sweng_stories.stories_manager.domain.Storia;

import java.util.ArrayList;

public interface OpStoria {

    public Storia getStoriaConID(int idStoria);
    public ArrayList<Storia> getAllStorie();
    public ArrayList<Storia> getStoriaConUsername(String username);
    public Storia inserisciStoria(Storia storia);
    public Scenario modificaScenario(int idScenario, int idStoria, String nuovoTesto);
    public Scenario getScenario(int idScenario, int idStoria);
    public boolean inserisciScenario(Scenario scenario);
    public ArrayList<Scenario> getScenariStoria(int idStoria);
}
