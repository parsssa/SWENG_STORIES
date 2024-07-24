// HttpController.java
package com.sweng_stories.stories_manager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.sweng_stories.stories_manager.domain.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    @PostMapping(value = "/storie", consumes = { "multipart/form-data" })
    public ResponseEntity<Storia> createStoria(
            @RequestParam("titolo") String titolo,
            @RequestParam("descrizione") String descrizione,
            @RequestParam("inizioDescrizione") String inizioDescrizione,
            @RequestParam("riddle") String riddle,
            @RequestParam("riddleType") String riddleType,
            @RequestParam("riddleQuestion") String riddleQuestion,
            @RequestParam("riddleAnswer") String riddleAnswer,
            @RequestParam("inventory") String inventory,
            @RequestParam Map<String, String> allParams) {
    
        List<String> finaliDescrizioni = allParams.entrySet().stream()
                .filter(entry -> entry.getKey().startsWith("finali["))
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
    
        List<Scenario> finali = finaliDescrizioni.stream().map(desc -> {
            Scenario scenario = new Scenario();
            scenario.setDescrizione(desc);
            // Aggiungi altri campi se necessario
            return scenario;
        }).collect(Collectors.toList());
    
        List<String> scenariDescrizioni = allParams.entrySet().stream()
                .filter(entry -> entry.getKey().startsWith("scenari[") && entry.getKey().endsWith("].descrizione"))
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
    
        List<Scenario> scenari = scenariDescrizioni.stream().map(desc -> {
            Scenario scenario = new Scenario();
            scenario.setDescrizione(desc);
            // Aggiungi altri campi se necessario
            return scenario;
        }).collect(Collectors.toList());
    
        // Estrai gli oggetti per ciascuno scenario
        scenari.forEach(scenario -> {
            String keyPrefix = "scenari[" + scenari.indexOf(scenario) + "].oggetti";
            List<Oggetto> oggetti = allParams.entrySet().stream()
                    .filter(entry -> entry.getKey().startsWith(keyPrefix))
                    .map(Map.Entry::getValue)
                    .map(itemName -> {
                        Oggetto oggetto = new Oggetto();
                        oggetto.setNome(itemName);
                        oggetto.setDescrizione(""); // Set a default description or modify as needed
                        return oggetto;
                    }).collect(Collectors.toList());
            scenario.setOggetti(oggetti);
        });
    
        // Creazione dell'oggetto Storia
        Storia storia = new Storia();
        storia.setTitolo(titolo);
        storia.setDescrizione(descrizione);
    
        Scenario inizio = new Scenario();
        inizio.setDescrizione(inizioDescrizione);
        storia.setInizio(inizio);
    
        Indovinello indovinello;
        if ("text".equals(riddleType)) {
            indovinello = new IndovinelloTestuale(null, riddle, riddleQuestion, riddleAnswer, null);
        } else {
            indovinello = new IndovinelloNumerico(null, riddle, riddleQuestion, Integer.parseInt(riddleAnswer), null);
        }
        storia.setIndovinello(indovinello);
    
        Inventario inventario = new Inventario();
        List<Oggetto> oggettiInventario = List.of(inventory.split(",")).stream()
                .map(item -> {
                    Oggetto oggetto = new Oggetto();
                    oggetto.setNome(item.trim());
                    oggetto.setDescrizione("");
                    return oggetto;
                }).collect(Collectors.toList());
        inventario.setOggetti(oggettiInventario);
        storia.setInventario(inventario);
    
        storia.setFinali(finali);
        storia.setScenari(scenari);
    
        Storia nuovaStoria = null;
        try {
            nuovaStoria = mongoDbController.createStoria(storia);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
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