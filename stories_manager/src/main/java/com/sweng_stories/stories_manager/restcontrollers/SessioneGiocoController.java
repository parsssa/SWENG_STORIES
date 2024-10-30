package com.sweng_stories.stories_manager.restcontrollers;

import com.sweng_stories.stories_manager.domain.Inventario;
import com.sweng_stories.stories_manager.domain.Scenario;
import com.sweng_stories.stories_manager.domain.SessioneGioco;
import com.sweng_stories.stories_manager.services.OpSessioneGioco;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/sessioni")
public class SessioneGiocoController {

    @Autowired
    private OpSessioneGioco serviceSessioneGioco;

    @PutMapping("/SessioneGioco/{idPartita}/scenari/{idScenario}/indovinello")
    public Scenario elaboraIndovinello(
            @PathVariable int idScenario,
            @RequestParam String risposta,
            @PathVariable int idPartita) {
        return serviceSessioneGioco.elaboraIndovinello(idScenario, risposta, idPartita);
    }

    @PutMapping("/SessioneGioco/{idPartita}/scenari/{idScenario}/alternativa")
    public Scenario elaboraAlternativa(
            @PathVariable int idScenario,
            @RequestParam String testoAlternativa,
            @PathVariable int idPartita) {
                Scenario scenario = serviceSessioneGioco.elaboraAlternativa(idScenario, testoAlternativa, idPartita);
                System.out.println(scenario);
                System.out.println(idPartita);
        return scenario;
    }

    @PostMapping("/SessioneGioco/{idPartita}/inventario")
    public Inventario raccogliOggetto(
            @PathVariable int idPartita,
            @RequestParam String oggetto) {
        return serviceSessioneGioco.raccogliOggetto(idPartita, oggetto);
    }

    @PostMapping("/SessioneGioco/")
    public SessioneGioco creaSessione(@RequestBody SessioneGioco partita) {
        return serviceSessioneGioco.creaSessione(partita);
    }

    @DeleteMapping("/SessioneGioco/{idPartita}")
    public boolean eliminaSessione(@PathVariable int idPartita) {
        return serviceSessioneGioco.eliminaSessione(idPartita);
    }

    @GetMapping("/SessioneGioco/utente/{username}")
    public ArrayList<SessioneGioco> getSessioniUtente(@PathVariable String username) {
        return serviceSessioneGioco.getSessioniUtente(username);
    }

    @GetMapping("/SessioneGioco/{idPartita}")
    public SessioneGioco getSessioneConID(@PathVariable int idPartita) {
        return serviceSessioneGioco.getSessioneConID(idPartita);
    }
}
