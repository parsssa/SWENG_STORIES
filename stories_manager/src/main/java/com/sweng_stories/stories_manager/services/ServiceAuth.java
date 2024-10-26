package com.sweng_stories.stories_manager.services;

import com.sweng_stories.stories_manager.dao.OpAuthDao;
import com.sweng_stories.stories_manager.domain.Utente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServiceAuth implements OpAutenticazione {
    @Autowired
    OpAuthDao authDao;
    @Override
    public boolean registraUtente(String username, String password) {
        Utente utente = authDao.getUtente(username);

        if(utente != null)
            return false;

        return authDao.registraUtente(username,password);
    }

    @Override
    public boolean loginUtente(String username, String password) {
        Utente utente = authDao.getUtente(username);
        if(utente == null)
            return false;

        if(utente.getUsername().equals(username) && utente.getPassword().equals(password)){
            return true;
        }

        return false;
    }

    @Override
    public Utente getUtente(String username) {
        return authDao.getUtente(username);
    }
}
