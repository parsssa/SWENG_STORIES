package com.sweng_stories.stories_manager.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sweng_stories.stories_manager.domain.Storia;
import com.sweng_stories.stories_manager.domain.Utente;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class JsonController {

    private static final String DATABASE_PATH = "database.json";
    private final File databaseFile = new File(DATABASE_PATH);
    private final ObjectMapper objectMapper;
    private List<Utente> utenti; // Aggiunta dichiarazione utenti
    private List<Storia> storie; // Aggiunta della dichiarazione per le storie


    public JsonController(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        loadData();
    }

    private void loadData() {
        try {
            if (!databaseFile.exists() || databaseFile.length() == 0) {
                utenti = new ArrayList<>();
                storie = new ArrayList<>();
                saveData(); // Assicura la creazione del file con una struttura vuota iniziale
            } else {
                DatabaseWrapper wrapper = objectMapper.readValue(databaseFile, DatabaseWrapper.class);
                utenti = Optional.ofNullable(wrapper.getUtenti()).orElseGet(ArrayList::new);
                storie = Optional.ofNullable(wrapper.getStorie()).orElseGet(ArrayList::new);
            }
        } catch (IOException e) {
            e.printStackTrace();
            utenti = new ArrayList<>();
            storie = new ArrayList<>();
        }
    }

    private void saveData() throws IOException {
        DatabaseWrapper wrapper = new DatabaseWrapper(utenti, storie);
        objectMapper.writeValue(databaseFile, wrapper);
    }

    public List<Storia> getAllStorie() {
        try {
            return objectMapper.readValue(databaseFile, new TypeReference<List<Storia>>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Impossibile recuperare l'elenco delle storie. Riprova più tardi.");
        }
    }

    public Storia getStoriaById(Long id) {
        List<Storia> storie = getAllStorie();
        Optional<Storia> storiaOptional = storie.stream().filter(s -> s.getId().equals(id)).findFirst();
        return storiaOptional.orElse(null);
    }

    public Storia createStoria(Storia nuovaStoria) {
        List<Storia> storie = getAllStorie();
        storie.add(nuovaStoria);
        writeStorieToFile(storie);
        return nuovaStoria;
    }

    public Storia updateStoria(Long id, Storia storiaAggiornata) {
        List<Storia> storie = getAllStorie();
        for (Storia storia : storie) {
            if (storia.getId().equals(id)) {
                storia.setTitolo(storiaAggiornata.getTitolo());
                storia.setDescrizione(storiaAggiornata.getDescrizione());
                storia.setInizio(storiaAggiornata.getInizio());
                storia.setFinali(storiaAggiornata.getFinali());
                storia.setScenari(storiaAggiornata.getScenari());
                storia.setInventario(storiaAggiornata.getInventario());
                // Aggiorna altri campi se necessario
                writeStorieToFile(storie);
                return storiaAggiornata;
            }
        }
        return null; // If storia with given ID is not found
    }

    public void deleteStoria(Long id) {
        List<Storia> storie = getAllStorie();
        storie.removeIf(storia -> storia.getId().equals(id));
        writeStorieToFile(storie);
    }

    public List<Utente> getAllUtenti() {
        return utenti;
    }

    public Utente getUtenteByUsername(String username) {
        try {
            // Leggi la lista degli utenti dal file JSON
            List<Utente> utenti = objectMapper.readValue(databaseFile, new TypeReference<List<Utente>>() {});

            // Verifica se la lista degli utenti è nulla e restituisci null se lo è
            if (utenti == null) {
                return null;
            }

            // Cerca l'utente con l'username specificato
            for (Utente utente : utenti) {
                if (utente.getUsername().equals(username)) {
                    return utente;
                }
            }

            // Se non viene trovato alcun utente con l'username specificato, restituisci null
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    public Utente createUtente(Utente nuovoUtente) {
        if (nuovoUtente == null || nuovoUtente.getUsername() == null || nuovoUtente.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("L'utente non può essere null e deve avere un username valido.");
        }

        // Verifica l'esistenza di un utente con lo stesso username
        Optional<Utente> utenteEsistente = utenti.stream()
                .filter(u -> u.getUsername().equals(nuovoUtente.getUsername()))
                .findFirst();
        if (utenteEsistente.isPresent()) {
            throw new IllegalStateException("Esiste già un utente con lo stesso username: " + nuovoUtente.getUsername());
        }

        utenti.add(nuovoUtente);
        try {
            saveData(); // Assicurati che saveData() gestisca la scrittura della lista aggiornata degli utenti nel file JSON
        } catch (IOException e) {
            throw new RuntimeException("Impossibile salvare l'utente nel database.", e);
        }
        return nuovoUtente;
    }

    public void deleteUtente(String username) {
        try {
            // Leggi la lista degli utenti dal file JSON
            List<Utente> utenti = objectMapper.readValue(databaseFile, new TypeReference<List<Utente>>() {
            });

            // Verifica se la lista degli utenti è nulla e inizializzala se necessario
            if (utenti == null) {
                utenti = new ArrayList<>();
            }

            // Rimuovi l'utente con l'username specificato dalla lista
            utenti.removeIf(utente -> utente.getUsername().equals(username));

            // Scrivi la lista aggiornata nel file JSON
            objectMapper.writeValue(databaseFile, utenti);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeStorieToFile(List<Storia> storie) {
        try {
            objectMapper.writeValue(databaseFile, storie);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeUtentiToFile() {
        try {
            objectMapper.writeValue(databaseFile, utenti);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Utente updateUtente(String username, Utente utenteAggiornato) {
        for (Utente utente : utenti) {
            if (utente.getUsername().equals(username)) {
                utente.setPassword(utenteAggiornato.getPassword());
                // Aggiorna altri campi se necessario
                writeUtentiToFile();
                return utenteAggiornato;
            }
        }
        return null; // If utente with given username is not found
    }

    public class DatabaseWrapper {
        private List<Utente> utenti;
        private List<Storia> storie;

        public DatabaseWrapper() {
            this.utenti = new ArrayList<>();
            this.storie = new ArrayList<>();
        }

        public DatabaseWrapper(List<Utente> utenti, List<Storia> storie) {
            this.utenti = utenti;
            this.storie = storie;
        }

        // Getters e Setters
        public List<Utente> getUtenti() {
            return utenti;
        }

        public void setUtenti(List<Utente> utenti) {
            this.utenti = utenti;
        }

        public List<Storia> getStorie() {
            return storie;
        }

        public void setStorie(List<Storia> storie) {
            this.storie = storie;
        }
    }

}
