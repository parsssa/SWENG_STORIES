package com.sweng_stories.stories_manager.dao;

import com.sweng_stories.stories_manager.domain.Utente;
import com.mongodb.client.MongoClients; // Importa MongoClients
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
public class AuthDao implements OpAuthDao {

    private final MongoCollection<Document> utentiCollection;

    @Autowired
    public AuthDao(MongoDatabase database) {
        this.utentiCollection = database.getCollection("utenti");
    }

    @Override
    public boolean registraUtente(String username, String password) {
        Document document = new Document("username", username)
                .append("password", password);
        utentiCollection.insertOne(document);
        return true;
    }

    @Override
    public Utente getUtente(String username) {
        Document query = new Document("username", username);
        Document result = utentiCollection.find(query).first();
        if (result != null) {
            return new Utente(result.getString("username"), result.getString("password"));
        }
        return null;
    }

    @Configuration
    static class MongoConfig {

        @Bean
        public MongoDatabase mongoDatabase() {
            MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017"); // Usa MongoClients per creare il client
            return mongoClient.getDatabase("nome_database"); // Sostituisci "nome_database" con il nome del tuo database
        }
    }
}
