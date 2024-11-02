package com.sweng_stories.stories_manager.dao;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.sweng_stories.stories_manager.domain.Inventario;
import com.sweng_stories.stories_manager.domain.SessioneGioco;
import com.sweng_stories.stories_manager.domain.Scenario;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

@Component
public class SessioneGiocoDao implements OpSessioneGiocoDao {

    private final MongoCollection<Document> sessioniCollection;

    @Autowired
    public SessioneGiocoDao(MongoDatabase database) {
        this.sessioniCollection = database.getCollection("sessioni");
    }

    @Override
    public SessioneGioco creaSessione(SessioneGioco partita) {
        ObjectId objectId = new ObjectId();

        // Gestione dell'inventario vuoto
        List<String> inventarioOggetti = (partita.getInventario() != null) ? partita.getInventario().getOggetti()
                : new ArrayList<>();

        Document document = new Document("_id", objectId)
                .append("username", partita.getUsername())
                .append("idStoria", partita.getIdStoria())
                .append("idScenarioCorrente", partita.getIdScenarioCorrente())
                .append("inventario", inventarioOggetti);

        sessioniCollection.insertOne(document);
        partita.setIdSessione(objectId.toString());
        System.out.println(partita.getIdSessione());
        System.out.println(partita.getIdSessione());
        System.out.println(partita.getIdSessione());

        return partita;
    }

    @Override
    public boolean eliminaSessione(String idPartita) {
        Document query = new Document("_id", new ObjectId(String.valueOf(idPartita)));
        return sessioniCollection.deleteOne(query).wasAcknowledged();
    }

    @Override
    public ArrayList<SessioneGioco> getSessioniUtente(String username) {
        List<Document> results = sessioniCollection.find(eq("username", username)).into(new ArrayList<>());
        ArrayList<SessioneGioco> sessioni = new ArrayList<>();
        for (Document result : results) {
            SessioneGioco sessione = new SessioneGioco(
                    result.getString("username"),
                    result.getInteger("idStoria"),
                    result.getInteger("idScenarioCorrente"),
                    new Inventario(result.getList("inventario", String.class)));
            sessione.setIdSessione(result.getObjectId("_id").toString());
            sessioni.add(sessione);
        }
        return sessioni;
    }

    @Override
    public SessioneGioco getSessioneConID(String idPartita) {
        // Converti l'ID di sessione in un ObjectId
        ObjectId objectId = new ObjectId(idPartita);
        Document query = new Document("_id", objectId);
        Document result = sessioniCollection.find(query).first();

        if (result != null) {
            SessioneGioco sessione = new SessioneGioco(
                    result.getString("username"),
                    result.getInteger("idStoria"),
                    result.getInteger("idScenarioCorrente"),
                    new Inventario(result.getList("inventario", String.class)));
            sessione.setIdSessione(result.getObjectId("_id").toString()); // Imposta idSessione come stringa usando
                                                                          // l'_id
            return sessione;
        }
        return null;
    }

    @Override
    public Scenario getScenarioCorrente(int idStoria, int idScenario) {
        Document query = new Document("idStoria", idStoria)
                .append("idScenarioCorrente", idScenario);
        Document result = sessioniCollection.find(query).first();
        if (result != null) {
            return result.get("scenarioCorrente", Scenario.class);
        }
        return null;
    }

    @Override
    public boolean aggiornaSessione(SessioneGioco partita) {
        Document query = new Document("_id", new ObjectId(String.valueOf(partita.getIdSessione())));
        Document update = new Document("$set", new Document("idScenarioCorrente", partita.getIdScenarioCorrente())
                .append("inventario", partita.getInventario().getOggetti()));
        return sessioniCollection.updateOne(query, update).wasAcknowledged();
    }
}