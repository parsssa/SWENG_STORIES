// HttpController.java
package com.sweng_stories.stories_manager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.sweng_stories.stories_manager.domain.Storia;
import com.sweng_stories.stories_manager.domain.Utente;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api")
public class HttpController {

    @Autowired
    private MongoDbController mongoDbController; // Cambio di istanza da JsonController a MongoDbController

    @GetMapping("/storie")
    public List<Storia> getAllStorie() {
        return mongoDbController.getAllStorie();
    }

    @GetMapping("/storie/{id}")
    public Storia getStoriaById(@PathVariable Long id) {
        return mongoDbController.getStoriaById(id);
    }

    @PostMapping("/storie")
    public Storia createStoria(@RequestBody Storia storia) {
        return mongoDbController.createStoria(storia);
    }

    @PutMapping("/storie/{id}")
    public Storia updateStoria(@PathVariable Long id, @RequestBody Storia storia) {
        return mongoDbController.updateStoria(id, storia);
    }

    @DeleteMapping("/storie/{id}")
    public void deleteStoria(@PathVariable Long id) {
        mongoDbController.deleteStoria(id);
    }

    @GetMapping("/utenti")
    public List<Utente> getAllUtenti() {
        return mongoDbController.getAllUtenti();
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Utente loginRequest) {
        Utente utente = mongoDbController.getUtenteByUsername(loginRequest.getUsername());
        if (utente != null && utente.getPassword().equals(loginRequest.getPassword())) {
            return ResponseEntity.ok(utente);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenziali non valide");
        }
    }

    @GetMapping("/utenti/{username}")
    public Utente getUtenteByUsername(@PathVariable String username) {
        return mongoDbController.getUtenteByUsername(username);
    }

    @PostMapping("/utenti")
    public ResponseEntity<?> createUtente(@RequestBody Utente utente) {
        Utente existingUtente = mongoDbController.getUtenteByUsername(utente.getUsername());
        if (existingUtente != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username già esistente");
        }
        Utente newUtente = mongoDbController.createUtente(utente);
        return ResponseEntity.status(HttpStatus.CREATED).body(newUtente);
    }

    @PutMapping("/utenti/{username}")
    public Utente updateUtente(@PathVariable String username, @RequestBody Utente utente) {
        return mongoDbController.updateUtente(username, utente);
    }

    @DeleteMapping("/utenti/{username}")
    public void deleteUtente(@PathVariable String username) {
        mongoDbController.deleteUtente(username);
    }
}
