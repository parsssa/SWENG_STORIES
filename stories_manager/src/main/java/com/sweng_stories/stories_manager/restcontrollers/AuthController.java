package com.sweng_stories.stories_manager.restcontrollers;

import com.sweng_stories.stories_manager.domain.Utente;
import com.sweng_stories.stories_manager.services.OpAutenticazione;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api")
public class AuthController implements OpAutenticazione {
    @Autowired
    private OpAutenticazione serviceAuth;
    @Override
    public boolean registraUtente(String username, String password) {
        return serviceAuth.registraUtente(username, password);
    }

    @Override
    public boolean loginUtente(String username, String password) {
        return serviceAuth.loginUtente(username, password);
    }

    @Override
    public Utente getUtente(String username) {
        return serviceAuth.getUtente(username);
    }
}
