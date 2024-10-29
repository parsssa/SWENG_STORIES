package com.sweng_stories.stories_manager.restcontrollers;

import com.sweng_stories.stories_manager.domain.Scenario;
import com.sweng_stories.stories_manager.domain.Storia;
import com.sweng_stories.stories_manager.services.OpStoria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/api/storie")
public class StoriaController {

    @Autowired
    private OpStoria serviceStoria;

    @GetMapping("/{id}")
    public Storia getStoriaConID(@PathVariable int id) {
        System.out.println("id storia " + id);
        return serviceStoria.getStoriaConID(id);
    }

    @GetMapping
    public ArrayList<Storia> getAllStorie() {
        return serviceStoria.getAllStorie();
    }

    @GetMapping("/utente/{username}")
    public ArrayList<Storia> getStoriaConUsername(@PathVariable String username) {
        return serviceStoria.getStoriaConUsername(username);
    }


    @PostMapping
    public ResponseEntity<Storia> inserisciStoria(@RequestBody Storia storia) {
        System.out.println(storia);
        try {
            Storia nuovaStoria = serviceStoria.inserisciStoria(storia);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuovaStoria);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{idStoria}/scenari/{idScenario}")
    public Scenario modificaScenario(
            @PathVariable int idScenario,
            @PathVariable int idStoria,
            @RequestParam String nuovoTesto) {
        return serviceStoria.modificaScenario(idScenario, idStoria, nuovoTesto);
    }

    @GetMapping("/{idStoria}/scenari/{idScenario}")
    public Scenario getScenario(
            @PathVariable int idScenario,
            @PathVariable int idStoria) {
        return serviceStoria.getScenario(idScenario, idStoria);
    }

    @PostMapping("/scenari")
    public boolean inserisciScenario(@RequestBody Scenario scenario) {
        return serviceStoria.inserisciScenario(scenario);
    }

    @GetMapping("/{idStoria}/scenari")
    public ArrayList<Scenario> getScenariStoria(@PathVariable int idStoria) {
        return serviceStoria.getScenariStoria(idStoria);
    }
}
