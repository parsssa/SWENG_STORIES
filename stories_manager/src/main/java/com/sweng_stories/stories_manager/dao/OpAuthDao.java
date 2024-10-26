package com.sweng_stories.stories_manager.dao;

import com.sweng_stories.stories_manager.domain.Utente;

public interface OpAuthDao {
    public boolean registraUtente(String username, String password);
    public Utente getUtente(String username);
}
