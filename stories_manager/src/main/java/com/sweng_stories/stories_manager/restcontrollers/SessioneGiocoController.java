package com.sweng_stories.stories_manager.restcontrollers;

import com.sweng_stories.stories_manager.domain.Inventario;
import com.sweng_stories.stories_manager.domain.Scenario;
import com.sweng_stories.stories_manager.domain.SessioneGioco;
import com.sweng_stories.stories_manager.services.OpSessioneGioco;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api")
public class SessioneGiocoController implements OpSessioneGioco {
    @Autowired
    OpSessioneGioco serviceSessioneGioco;
    @Override
    public Scenario elaboraIndovinello(int idScenario, String risposta, int idPartita) {
        return serviceSessioneGioco.elaboraIndovinello(idScenario, risposta, idPartita);
    }

    @Override
    public Scenario elaboraAlternativa(int idScenario, int idScelta, int idPartita) {
        return serviceSessioneGioco.elaboraAlternativa(idScenario, idScelta, idPartita);
    }

    @Override
    public Inventario raccogliOggetto(int idPartita, String oggetto) {
        return serviceSessioneGioco.raccogliOggetto(idPartita, oggetto);
    }

    @Override
    public SessioneGioco creaSessione(SessioneGioco partita) {
        return serviceSessioneGioco.creaSessione(partita);
    }

    @Override
    public boolean eliminaSessione(int idPartita) {
        return serviceSessioneGioco.eliminaSessione(idPartita);
    }

    @Override
    public ArrayList<SessioneGioco> getSessioniUtente(String username) {
        return serviceSessioneGioco.getSessioniUtente(username);
    }

    @Override
    public SessioneGioco getSessioneConID(int idPartita) {
        return serviceSessioneGioco.getSessioneConID(idPartita);
    }

    @Override
    public Scenario getScenarioAttuale(int idStoria, int idScenario) {
        return serviceSessioneGioco.getScenarioAttuale(idStoria, idScenario);
    }

    @Override
    public boolean aggiornaPartita(SessioneGioco partita) {
        return serviceSessioneGioco.aggiornaPartita(partita);
    }
}
