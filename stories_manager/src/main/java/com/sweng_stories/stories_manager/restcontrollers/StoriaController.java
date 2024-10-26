package com.sweng_stories.stories_manager.restcontrollers;

import com.sweng_stories.stories_manager.domain.Scenario;
import com.sweng_stories.stories_manager.domain.Storia;
import com.sweng_stories.stories_manager.services.OpStoria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/storie")
public class StoriaController {

    @Autowired
    private OpStoria serviceStoria;

    @GetMapping("/storie/{id}")
    public Storia getStoriaConID(@PathVariable int id) {
        return serviceStoria.getStoriaConID(id);
    }

    @GetMapping("/storie")
    public ArrayList<Storia> getAllStorie() {
        return serviceStoria.getAllStorie();
    }

    @GetMapping("/utente/{username}")
    public ArrayList<Storia> getStoriaConUsername(@PathVariable String username) {
        return serviceStoria.getStoriaConUsername(username);
    }

    @PostMapping("/storie")
    public ResponseEntity<Storia> inserisciStoria(@RequestBody Storia storia) {
    try {
        Storia nuovaStoria = serviceStoria.inserisciStoria(storia);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuovaStoria);
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}

    @PutMapping("/storie/{idStoria}/scenari/{idScenario}")
    public Scenario modificaScenario(
            @PathVariable int idScenario,
            @PathVariable int idStoria,
            @RequestParam String nuovoTesto) {
        return serviceStoria.modificaScenario(idScenario, idStoria, nuovoTesto);
    }

    @GetMapping("/storie/{idStoria}/scenari/{idScenario}")
    public Scenario getScenario(
            @PathVariable int idScenario,
            @PathVariable int idStoria) {
        return serviceStoria.getScenario(idScenario, idStoria);
    }

    @PostMapping("/storie/{idStoria}/scenari")
    public boolean inserisciScenario(@RequestBody Scenario scenario) {
        return serviceStoria.inserisciScenario(scenario);
    }

    @GetMapping("/storie/{idStoria}/scenari")
    public ArrayList<Scenario> getScenariStoria(@PathVariable int idStoria) {
        return serviceStoria.getScenariStoria(idStoria);
    }
}
