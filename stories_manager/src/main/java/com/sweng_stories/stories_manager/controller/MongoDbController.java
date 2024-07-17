// MongoDbController.java
package com.sweng_stories.stories_manager.controller;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.sweng_stories.stories_manager.domain.*;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MongoDbController {

    private final MongoClient mongoClient;
    private final MongoDatabase database;
    private final MongoCollection<Document> utentiCollection;
    private final MongoCollection<Document> storieCollection;
    private final MongoCollection<Document> scenariCollection;
    private final MongoCollection<Document> indovinelliCollection;
    private final MongoCollection<Document> oggettiCollection;

    public MongoDbController(@Value("${spring.data.mongodb.host}") String mongoHost,
                             @Value("${spring.data.mongodb.port}") int mongoPort,
                             @Value("${spring.data.mongodb.database}") String databaseName) {

        String connectionString = String.format("mongodb://%s:%d", mongoHost, mongoPort);

        // Creazione dell'istanza del client MongoDB
        this.mongoClient = MongoClients.create(connectionString);

        // Creazione del database se non esiste
        this.database = mongoClient.getDatabase(databaseName);

        // Creazione delle collezioni se non esistono
        this.utentiCollection = database.getCollection("utenti");
        if (utentiCollection == null) {
            database.createCollection("utenti");
        }

        this.storieCollection = database.getCollection("storie");
        if (storieCollection == null) {
            database.createCollection("storie");
        }

        this.scenariCollection = database.getCollection("scenari");
        if (scenariCollection == null) {
            database.createCollection("scenari");
        }

        this.indovinelliCollection = database.getCollection("indovinelli");
        if (indovinelliCollection == null) {
            database.createCollection("indovinelli");
        }

        this.oggettiCollection = database.getCollection("oggetti");
        if (oggettiCollection == null) {
            database.createCollection("oggetti");
        }
    }

    // Metodi per le storie
    public List<Storia> getAllStorie() {
        List<Storia> storie = new ArrayList<>();
        storieCollection.find().forEach(document -> {
            Storia storia = new Storia();
            storia.setId(document.getLong("id"));
            storia.setTitolo(document.getString("titolo"));
            storia.setContenuto(document.getString("contenuto"));
            // Aggiungi altri campi se necessario
            storie.add(storia);
        });
        return storie;
    }

    public Storia getStoriaById(Long id) {
        Document query = new Document("id", id);
        Document result = storieCollection.find(query).first();
        if (result != null) {
            Storia storia = new Storia();
            storia.setId(result.getLong("id"));
            storia.setTitolo(result.getString("titolo"));
            storia.setContenuto(result.getString("contenuto"));
            // Aggiungi altri campi se necessario
            return storia;
        }
        return null;
    }

    public Storia createStoria(Storia nuovaStoria) {
        Document document = new Document("id", nuovaStoria.getId())
                .append("titolo", nuovaStoria.getTitolo())
                .append("contenuto", nuovaStoria.getContenuto());
        // Aggiungi altri campi se necessario
        storieCollection.insertOne(document);
        return nuovaStoria;
    }

    public Storia updateStoria(Long id, Storia storiaAggiornata) {
        Document query = new Document("id", id);
        Document update = new Document("$set", new Document("titolo", storiaAggiornata.getTitolo())
                .append("contenuto", storiaAggiornata.getContenuto()));
        // Aggiungi altri campi se necessario
        storieCollection.updateOne(query, update);
        return storiaAggiornata;
    }

    public void deleteStoria(Long id) {
        Document query = new Document("id", id);
        storieCollection.deleteOne(query);
    }

    // Metodi per gli utenti
    public List<Utente> getAllUtenti() {
        List<Utente> utenti = new ArrayList<>();
        utentiCollection.find().forEach(document -> {
            Utente utente = new Utente();
            utente.setUsername(document.getString("username"));
            utente.setPassword(document.getString("password"));
            // Aggiungi altri campi se necessario
            utenti.add(utente);
        });
        return utenti;
    }

    public Utente getUtenteByUsername(String username) {
        Document query = new Document("username", username);
        Document result = utentiCollection.find(query).first();
        if (result != null) {
            Utente utente = new Utente();
            utente.setUsername(result.getString("username"));
            utente.setPassword(result.getString("password"));
            // Aggiungi altri campi se necessario
            return utente;
        }
        return null;
    }

    public Utente createUtente(Utente nuovoUtente) {
        Document document = new Document("username", nuovoUtente.getUsername())
                .append("password", nuovoUtente.getPassword());
        // Aggiungi altri campi se necessario
        utentiCollection.insertOne(document);
        return nuovoUtente;
    }

    public void deleteUtente(String username) {
        Document query = new Document("username", username);
        utentiCollection.deleteOne(query);
    }

    public Utente updateUtente(String username, Utente utenteAggiornato) {
        Document query = new Document("username", username);
        Document update = new Document("$set", new Document("password", utenteAggiornato.getPassword()));
        utentiCollection.updateOne(query, update);
        return utenteAggiornato;
    }

    // Metodi per gli scenari
    public List<Scenario> getAllScenari() {
        List<Scenario> scenari = new ArrayList<>();
        scenariCollection.find().forEach(document -> {
            Scenario scenario = new Scenario();
            scenario.setId(document.getLong("id"));
            scenario.setDescrizione(document.getString("descrizione"));
            // Recupera indovinelli e oggetti associati
            List<Indovinello> indovinelli = getIndovinelliByScenarioId(scenario.getId());
            scenario.setIndovinelli(indovinelli);
            List<Oggetto> oggetti = getOggettiByScenarioId(scenario.getId());
            scenario.setOggetti(oggetti);
            // Aggiungi altri campi se necessario
            scenari.add(scenario);
        });
        return scenari;
    }

    public Scenario getScenarioById(Long id) {
        Document query = new Document("id", id);
        Document result = scenariCollection.find(query).first();
        if (result != null) {
            Scenario scenario = new Scenario();
            scenario.setId(result.getLong("id"));
            scenario.setDescrizione(result.getString("descrizione"));
            // Recupera indovinelli e oggetti associati
            List<Indovinello> indovinelli = getIndovinelliByScenarioId(scenario.getId());
            scenario.setIndovinelli(indovinelli);
            List<Oggetto> oggetti = getOggettiByScenarioId(scenario.getId());
            scenario.setOggetti(oggetti);
            // Aggiungi altri campi se necessario
            return scenario;
        }
        return null;
    }

    public Scenario createScenario(Scenario nuovoScenario) {
        Document document = new Document("id", nuovoScenario.getId())
                .append("descrizione", nuovoScenario.getDescrizione());
        // Aggiungi altri campi se necessario
        scenariCollection.insertOne(document);
        // Salva anche indovinelli e oggetti associati
        saveIndovinelliAndOggettiForScenario(nuovoScenario);
        return nuovoScenario;
    }

    public Scenario updateScenario(Long id, Scenario scenarioAggiornato) {
        Document query = new Document("id", id);
        Document update = new Document("$set", new Document("descrizione", scenarioAggiornato.getDescrizione()));
        // Aggiungi altri campi se necessario
        scenariCollection.updateOne(query, update);
        // Aggiorna indovinelli e oggetti associati
        updateIndovinelliAndOggettiForScenario(id, scenarioAggiornato);
        return scenarioAggiornato;
    }

    public void deleteScenario(Long id) {
        Document query = new Document("id", id);
        scenariCollection.deleteOne(query);
        // Rimuovi anche indovinelli e oggetti associati
        deleteIndovinelliAndOggettiForScenario(id);
    }

    // Metodi per gli indovinelli
    public List<Indovinello> getIndovinelliByScenarioId(Long scenarioId) {
        List<Indovinello> indovinelli = new ArrayList<>();
        Document query = new Document("scenarioId", scenarioId);
        indovinelliCollection.find(query).forEach(document -> {
            String tipo = document.getString("tipo");
            Long id = document.getLong("id");
            String descrizione = document.getString("descrizione");
            Object rispostaCorretta = document.get("rispostaCorretta");

            Indovinello indovinello = IndovinelloFactory.createIndovinello(tipo, id, descrizione, rispostaCorretta);
            // Add other fields if necessary
            indovinelli.add(indovinello);
        });
        return indovinelli;
    }

    public Indovinello getIndovinelloById(Long id) {
        Document query = new Document("id", id);
        Document result = indovinelliCollection.find(query).first();
        if (result != null) {
            String tipo = result.getString("tipo");
            Long idResult = result.getLong("id");
            String descrizione = result.getString("descrizione");
            Object rispostaCorretta = result.get("rispostaCorretta");

            return IndovinelloFactory.createIndovinello(tipo, idResult, descrizione, rispostaCorretta);
        }
        return null;
    }

    public Indovinello createIndovinello(Indovinello nuovoIndovinello) {
        Document document = new Document("id", nuovoIndovinello.getId())
                .append("descrizione", nuovoIndovinello.getDescrizione());

        if (nuovoIndovinello instanceof IndovinelloTestuale) {
            document.append("tipo", "testuale")
                    .append("rispostaCorretta", ((IndovinelloTestuale) nuovoIndovinello).getRispostaCorretta());
        } else if (nuovoIndovinello instanceof IndovinelloNumerico) {
            document.append("tipo", "numerico")
                    .append("rispostaCorretta", ((IndovinelloNumerico) nuovoIndovinello).getRispostaCorretta());
        }
        // Add other fields if necessary
        indovinelliCollection.insertOne(document);
        return nuovoIndovinello;
    }

    public Indovinello updateIndovinello(Long id, Indovinello indovinelloAggiornato) {
        Document query = new Document("id", id);
        Document update = new Document("$set", new Document("descrizione", indovinelloAggiornato.getDescrizione()));
        
        if (indovinelloAggiornato instanceof IndovinelloTestuale) {
            update.append("$set", new Document("tipo", "testuale")
                    .append("rispostaCorretta", ((IndovinelloTestuale) indovinelloAggiornato).getRispostaCorretta()));
        } else if (indovinelloAggiornato instanceof IndovinelloNumerico) {
            update.append("$set", new Document("tipo", "numerico")
                    .append("rispostaCorretta", ((IndovinelloNumerico) indovinelloAggiornato).getRispostaCorretta()));
        }
        // Add other fields if necessary
        indovinelliCollection.updateOne(query, update);
        return indovinelloAggiornato;
    }

    public void deleteIndovinello(Long id) {
        Document query = new Document("id", id);
        indovinelliCollection.deleteOne(query);
    }

    // Metodi per gli oggetti
    public List<Oggetto> getOggettiByScenarioId(Long scenarioId) {
        List<Oggetto> oggetti = new ArrayList<>();
        Document query = new Document("scenarioId", scenarioId);
        oggettiCollection.find(query).forEach(document -> {
            Oggetto oggetto = new Oggetto();
            oggetto.setId(document.getLong("id"));
            oggetto.setNome(document.getString("nome"));
            oggetto.setDescrizione(document.getString("descrizione"));
            // Aggiungi altri campi se necessario
            oggetti.add(oggetto);
        });
        return oggetti;
    }

    public Oggetto getOggettoById(Long id) {
        Document query = new Document("id", id);
        Document result = oggettiCollection.find(query).first();
        if (result != null) {
            Oggetto oggetto = new Oggetto();
            oggetto.setId(result.getLong("id"));
            oggetto.setNome(result.getString("nome"));
            oggetto.setDescrizione(result.getString("descrizione"));
            // Aggiungi altri campi se necessario
            return oggetto;
        }
        return null;
    }

    public Oggetto createOggetto(Oggetto nuovoOggetto) {
        Document document = new Document("id", nuovoOggetto.getId())
                .append("nome", nuovoOggetto.getNome())
                .append("descrizione", nuovoOggetto.getDescrizione());
        // Aggiungi altri campi se necessario
        oggettiCollection.insertOne(document);
        return nuovoOggetto;
    }

    public Oggetto updateOggetto(Long id, Oggetto oggettoAggiornato) {
        Document query = new Document("id", id);
        Document update = new Document("$set", new Document("nome", oggettoAggiornato.getNome())
                .append("descrizione", oggettoAggiornato.getDescrizione()));
        // Aggiungi altri campi se necessario
        oggettiCollection.updateOne(query, update);
        return oggettoAggiornato;
    }

    public void deleteOggetto(Long id) {
        Document query = new Document("id", id);
        oggettiCollection.deleteOne(query);
    }

    // Metodi di supporto per gli scenari
    private void saveIndovinelliAndOggettiForScenario(Scenario scenario) {
        for (Indovinello indovinello : scenario.getIndovinelli()) {
            Document document = new Document("scenarioId", scenario.getId())
                    .append("id", indovinello.getId())
                    .append("descrizione", indovinello.getDescrizione());
            if (indovinello instanceof IndovinelloTestuale) {
                document.append("tipo", "testuale")
                        .append("rispostaCorretta", ((IndovinelloTestuale) indovinello).getRispostaCorretta());
            } else if (indovinello instanceof IndovinelloNumerico) {
                document.append("tipo", "numerico")
                        .append("rispostaCorretta", ((IndovinelloNumerico) indovinello).getRispostaCorretta());
            }
            indovinelliCollection.insertOne(document);
        }
        for (Oggetto oggetto : scenario.getOggetti()) {
            Document document = new Document("scenarioId", scenario.getId())
                    .append("id", oggetto.getId())
                    .append("nome", oggetto.getNome())
                    .append("descrizione", oggetto.getDescrizione());
            oggettiCollection.insertOne(document);
        }
    }

    private void updateIndovinelliAndOggettiForScenario(Long scenarioId, Scenario scenario) {
        // Rimuovere indovinelli e oggetti esistenti
        indovinelliCollection.deleteMany(new Document("scenarioId", scenarioId));
        oggettiCollection.deleteMany(new Document("scenarioId", scenarioId));
        
        // Salvare gli aggiornamenti
        saveIndovinelliAndOggettiForScenario(scenario);
    }

    private void deleteIndovinelliAndOggettiForScenario(Long scenarioId) {
        // Rimuovere indovinelli e oggetti associati
        indovinelliCollection.deleteMany(new Document("scenarioId", scenarioId));
        oggettiCollection.deleteMany(new Document("scenarioId", scenarioId));
    }
}