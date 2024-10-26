package com.sweng_stories.stories_manager.services;

import com.sweng_stories.stories_manager.domain.Utente;

public interface OpAutenticazione {

    public boolean registraUtente(String username, String password);
    public boolean loginUtente(String username, String password);
    public Utente getUtente(String username);
}
