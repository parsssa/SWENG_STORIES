// MongoDbController.java
package com.sweng_stories.stories_manager.controller;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.sweng_stories.stories_manager.domain.Storia;
import com.sweng_stories.stories_manager.domain.Utente;
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

    public MongoDbController(@Value("${spring.data.mongodb.host}") String mongoHost,
                             @Value("${spring.data.mongodb.port}") int mongoPort,
                             @Value("${spring.data.mongodb.database}") String databaseName) {

        String connectionString = String.format("mongodb://%s:%d", mongoHost, mongoPort);

        // Creazione dell'istanza del client MongoDB
        this.mongoClient = MongoClients.create(connectionString);

        // Creazione del database se non esiste
        this.database = mongoClient.getDatabase(databaseName);

        // Creazione delle collezioni utenti e storie se non esistono
        this.utentiCollection = database.getCollection("utenti");
        if (utentiCollection == null) {
            database.createCollection("utenti");
        }

        this.storieCollection = database.getCollection("storie");
        if (storieCollection == null) {
            database.createCollection("storie");
        }
    }

    public MongoDatabase getDatabase() {
        return this.database;
    }

    public void writeToDatabase(String collectionName, String jsonDocument) {
        // Conversione della stringa JSON in un Document MongoDB
        Document document = Document.parse(jsonDocument);
    
        // Ottenimento della collezione specificata
        MongoCollection<Document> collection = this.database.getCollection(collectionName);
    
        // Inserimento del documento nella collezione
        collection.insertOne(document);
    }
    
    

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
    
}
