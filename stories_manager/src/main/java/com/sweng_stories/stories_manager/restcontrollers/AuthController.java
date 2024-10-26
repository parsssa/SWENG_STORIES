package com.sweng_stories.stories_manager.restcontrollers;

import com.sweng_stories.stories_manager.domain.Utente;
import com.sweng_stories.stories_manager.services.OpAutenticazione;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private OpAutenticazione serviceAuth;

    @PostMapping("/AuthController/register")
    public boolean registraUtente(
            @RequestParam String username,
            @RequestParam String password) {
        return serviceAuth.registraUtente(username, password);
    }

    @PostMapping("/AuthController/login")
    public boolean loginUtente(
            @RequestParam String username,
            @RequestParam String password) {
        return serviceAuth.loginUtente(username, password);
    }

    @GetMapping("/AuthController/user/{username}")
    public Utente getUtente(@PathVariable String username) {
        return serviceAuth.getUtente(username);
    }
}
