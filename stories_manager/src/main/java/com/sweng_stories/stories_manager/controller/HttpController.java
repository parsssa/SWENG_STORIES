// HttpController.java
package com.sweng_stories.stories_manager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.sweng_stories.stories_manager.domain.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api")
public class HttpController {

    @Autowired
    private MongoDbController mongoDbController;

    @GetMapping("/storie")
    public List<Storia> getAllStorie() {
        return mongoDbController.getAllStorie();
    }

    @GetMapping("/storie/titoli")
    public List<String> getAllStorieTitoli() {
        return mongoDbController.getAllStorieTitoli();
    }

    @GetMapping("/storie/{id}")
    public ResponseEntity<Storia> getStoriaById(@PathVariable String id) {
        try {
            System.out.println("SONO NELLA PARTE LONG");
            Long storiaId = Long.parseLong(id);
            Storia storia = mongoDbController.getStoriaById(storiaId);
            if (storia != null) {
                System.out.println("STORIA NULLLLLLL");

                return ResponseEntity.ok(storia);
            } else {
                System.out.println("BUILDATOOOOOOOOOOOOO");

                return ResponseEntity.notFound().build();
            }
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body(null); // Ritorna una risposta di bad request se l'ID non è un
                                                           // numero valido
        }
    }

    @PostMapping("/storie")
    public ResponseEntity<Storia> createStoria(@RequestBody Storia storia) {
        if (storia == null) {
            System.out.println("NON Ricevuto storia: \n \n \n \n \n \n \n \n" + storia + " ds \n \n \n \n \n \n \n \n");
            System.out.println("NON Ricevuto storia: \n \n \n \n \n \n \n \n" + storia + " ds \n \n \n \n \n \n \n \n");
            return ResponseEntity.badRequest().build();
        }
        System.out.println("Ricevuto storia: \n \n \n \n \n \n \n \n" + storia + " ds \n \n \n \n \n \n \n \n"); // Aggiungi
                                                                                                                 // questo
                                                                                                                 // per
                                                                                                                 // debug
        Storia nuovaStoria = mongoDbController.createStoria(storia);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuovaStoria);
    }

    @PutMapping("/storie/{id}")
    public ResponseEntity<Storia> updateStoria(@PathVariable Long id, @RequestBody Storia storia) {
        if (storia == null || !id.equals(storia.getId())) {
            return ResponseEntity.badRequest().build();
        }
        Storia storiaAggiornata = mongoDbController.updateStoria(id, storia);
        return ResponseEntity.ok(storiaAggiornata);
    }

    @DeleteMapping("/storie/{id}")
    public ResponseEntity<Void> deleteStoria(@PathVariable Long id) {
        mongoDbController.deleteStoria(id);
        return ResponseEntity.noContent().build();
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
    public ResponseEntity<Utente> getUtenteByUsername(@PathVariable String username) {
        Utente utente = mongoDbController.getUtenteByUsername(username);
        if (utente != null) {
            return ResponseEntity.ok(utente);
        } else {
            return ResponseEntity.notFound().build();
        }
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
    public ResponseEntity<Utente> updateUtente(@PathVariable String username, @RequestBody Utente utente) {
        if (utente == null || !username.equals(utente.getUsername())) {
            return ResponseEntity.badRequest().build();
        }
        Utente utenteAggiornato = mongoDbController.updateUtente(username, utente);
        return ResponseEntity.ok(utenteAggiornato);
    }

    @DeleteMapping("/utenti/{username}")
    public ResponseEntity<Void> deleteUtente(@PathVariable String username) {
        mongoDbController.deleteUtente(username);
        return ResponseEntity.noContent().build();
    }
}