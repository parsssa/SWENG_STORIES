// HttpController.java
package com.sweng_stories.stories_manager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.sweng_stories.stories_manager.domain.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import java.util.stream.Stream;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api")
public class HttpController {

    @Autowired
    private MongoDbController mongoDbController;

    // Storie Endpoints

    @GetMapping("/storie")
    public List<Storia> getAllStorie() {
        return mongoDbController.getAllStorie();
    }

    @GetMapping("/storie/{id}")
    public ResponseEntity<Storia> getStoriaById(@PathVariable String id) {
        try {
            Long storiaId = Long.parseLong(id);
            Storia storia = mongoDbController.getStoriaById(storiaId);
            if (storia != null) {
                return ResponseEntity.ok(storia);
            } else {
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
            @RequestParam Map<String, String> allParams) {

        // Creazione dell'oggetto Storia
        Storia storia = new Storia();
        storia.setTitolo(titolo);
        storia.setDescrizione(descrizione);

        // Inizio
        Scenario inizio = new Scenario();
        inizio.setDescrizione(inizioDescrizione);
        storia.setInizio(inizio);

        // Processa e popola i finali
        List<Scenario> finali = allParams.entrySet().stream()
                .filter(entry -> entry.getKey().startsWith("finali[") && entry.getKey().endsWith("descrizione"))
                .map(entry -> {
                    Scenario scenario = new Scenario();
                    scenario.setDescrizione(entry.getValue());
                    return scenario;
                }).collect(Collectors.toList());
        storia.setFinali(finali);

        // Processa e popola gli scenari
        List<Scenario> scenari = allParams.entrySet().stream()
                .filter(entry -> entry.getKey().startsWith("scenari[") && entry.getKey().endsWith("descrizione"))
                .map(entry -> {
                    Scenario scenario = new Scenario();
                    scenario.setDescrizione(entry.getValue());

                    // Estrazione delle alternative per ogni scenario
                    String scenarioIndex = entry.getKey().split("\\[")[1].split("\\]")[0];
                    List<Alternative> alternatives = allParams.entrySet().stream()
                            .filter(e -> e.getKey().startsWith("scenari[" + scenarioIndex + "].alternatives"))
                            .map(e -> {
                                Alternative alternative = new Alternative();
                                if (e.getKey().endsWith(".text")) {
                                    alternative.setText(e.getValue());
                                }
                                if (e.getKey().endsWith(".type")) {
                                    alternative.setType(e.getValue());
                                }
                                if (e.getKey().endsWith(".items")) {
                                    if (e.getValue() != null && !e.getValue().trim().isEmpty()) {
                                        alternative.setItems(List.of(e.getValue().split(",")));
                                    } else {
                                        alternative.setItems(new ArrayList<>()); // Inizializza una lista vuota
                                    }
                                }
                                // Gestione del collegamento a uno scenario o a un finale specifico
                                if (e.getKey().endsWith(".portaA")) {
                                    String leadsToValue = e.getValue();
                                    if (leadsToValue.startsWith("ending-")) {
                                        // Logica per i finali
                                        alternative.setNextScenarioId(null); // Impostare un valore speciale o gestirlo
                                                                             // separatamente
                                    } else {
                                        try {
                                            Long nextScenarioId = Long.parseLong(leadsToValue);
                                            alternative.setNextScenarioId(nextScenarioId);
                                        } catch (NumberFormatException ex) {
                                            ex.printStackTrace(); // Gestisci l'errore secondo necessità
                                        }
                                    }
                                }
                                return alternative;
                            }).collect(Collectors.toList());

                    scenario.setAlternatives(alternatives); // Aggiungi le alternative allo scenario

                    // Popola oggetti da alternative
                    scenario.setOggetti(alternatives.stream()
                            .flatMap((Alternative alt) -> alt.getItems() != null
                                    ? alt.getItems().stream()
                                    : Stream.<String>empty())
                            .map((String item) -> {
                                Oggetto oggetto = new Oggetto();
                                oggetto.setNome(item.trim());
                                return oggetto;
                            }).collect(Collectors.toList()));

                    // Gestione dell'indovinello specifico per scenario
                    if (allParams.containsKey("riddle")) {
                        String riddleType = allParams.get("riddleType");
                        Indovinello indovinello;
                        if ("text".equals(riddleType)) {
                            indovinello = new IndovinelloTestuale();
                            ((IndovinelloTestuale) indovinello).setDescrizione(allParams.get("riddle"));
                            ((IndovinelloTestuale) indovinello).setDomanda(allParams.get("riddleQuestion"));
                            ((IndovinelloTestuale) indovinello).setRispostaCorretta(allParams.get("riddleAnswer"));
                        } else if ("numeric".equals(riddleType)) {
                            indovinello = new IndovinelloNumerico();
                            ((IndovinelloNumerico) indovinello).setDescrizione(allParams.get("riddle"));
                            ((IndovinelloNumerico) indovinello).setDomanda(allParams.get("riddleQuestion"));
                            ((IndovinelloNumerico) indovinello)
                                    .setRispostaCorretta(Integer.parseInt(allParams.get("riddleAnswer")));
                        } else {
                            throw new IllegalArgumentException("Tipo di indovinello non riconosciuto");
                        }

                        // Aggiungi l'indovinello allo scenario
                        scenario.addIndovinello(indovinello);
                    }
                    return scenario;
                }).collect(Collectors.toList());
        storia.setScenari(scenari);


        // Creazione dell'inventario
        if (allParams.containsKey("inventory")) {
            storia.setInventarioFromItems(allParams.get("inventory"));
        }

        // Salva la storia nel database
        try {
            Storia nuovaStoria = mongoDbController.createStoria(storia);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuovaStoria);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
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

    @GetMapping("/storie/titoli")
    public ResponseEntity<List<StoriaTitoloDto>> getAllStorieTitoli() {
        List<Storia> storie = mongoDbController.getAllStorie();
        List<StoriaTitoloDto> storieTitoli = storie.stream()
                .map(storia -> new StoriaTitoloDto(storia.getId(), storia.getTitolo()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(storieTitoli);
    }

    // Utenti Endpoints

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

    // Classe DTO per restituire solo ID e titolo della storia
    public static class StoriaTitoloDto {
        private Long id;
        private String titolo;

        public StoriaTitoloDto(Long id, String titolo) {
            this.id = id;
            this.titolo = titolo;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getTitolo() {
            return titolo;
        }

        public void setTitolo(String titolo) {
            this.titolo = titolo;
        }
    }
}
