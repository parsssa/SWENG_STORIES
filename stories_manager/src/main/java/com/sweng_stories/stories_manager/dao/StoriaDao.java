package com.sweng_stories.stories_manager.dao;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.sweng_stories.stories_manager.domain.Alternativa;
import com.sweng_stories.stories_manager.domain.Indovinello;
import com.sweng_stories.stories_manager.domain.Scenario;
import com.sweng_stories.stories_manager.domain.Storia;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

@Component
public class StoriaDao implements OpStoriaDao {

    private final MongoCollection<Document> storieCollection;

    @Autowired
    public StoriaDao(MongoDatabase database) {
        this.storieCollection = database.getCollection("storie");
    }

    @Override
    public Storia getStoriaConID(int idStoria) {
        Document query = new Document("_id", new ObjectId(String.valueOf(idStoria)));
        Document result = storieCollection.find(query).first();
        if (result != null) {
            Storia storia = new Storia(
                    result.getString("titolo"),
                    result.getString("username")
            );
            storia.setId(result.getObjectId("_id").hashCode());
            storia.setInizio(result.get("inizio", Scenario.class));
            return storia;
        }
        return null;
    }

    @Override
    public ArrayList<Storia> getAllStorie() {
        List<Document> results = storieCollection.find().into(new ArrayList<>());
        ArrayList<Storia> storie = new ArrayList<>();
        for (Document result : results) {
            Storia storia = new Storia(
                    result.getString("titolo"),
                    result.getString("username")
            );
            storia.setId(result.getObjectId("_id").hashCode());
            storia.setInizio(result.get("inizio", Scenario.class));
            storie.add(storia);
        }
        return storie;
    }

    @Override
    public ArrayList<Storia> getStoriaConUsername(String username) {
        List<Document> results = storieCollection.find(eq("username", username)).into(new ArrayList<>());
        ArrayList<Storia> storie = new ArrayList<>();
        for (Document result : results) {
            Storia storia = new Storia(
                    result.getString("titolo"),
                    result.getString("username")
            );
            storia.setId(result.getObjectId("_id").hashCode());
            storia.setInizio(result.get("inizio", Scenario.class));
            storie.add(storia);
        }
        return storie;
    }

    @Override
    public Storia inserisciStoria(Storia storia) {
    ObjectId objectId = new ObjectId();

    // Creazione del documento per l'oggetto `Scenario`
    Document scenarioDoc = new Document()
            .append("idStoria", storia.getInizio().getIdStoria())
            .append("idScenario", storia.getInizio().getIdScenario())
            .append("testoScenario", storia.getInizio().getTestoScenario())
            .append("oggetto", storia.getInizio().getOggetto());

    // Creazione della lista di `Alternativa` come documenti
    List<Document> alternativeDocs = new ArrayList<>();
    for (Alternativa alternativa : storia.getInizio().getAlternative()) {
        Document alternativaDoc = new Document()
                .append("idScenario", alternativa.getIdScenario())
                .append("idScenarioSuccessivo", alternativa.getIdScenarioSuccessivo())
                .append("testoAlternativa", alternativa.getTestoAlternativa())
                .append("oggettoRichiesto", alternativa.getOggettoRichiesto());
        alternativeDocs.add(alternativaDoc);
    }
    scenarioDoc.append("alternative", alternativeDocs);

    // Aggiungi `Indovinello` se presente
    if (storia.getInizio().getIndovinello() != null) {
        Indovinello indovinello = storia.getInizio().getIndovinello();
        Document indovinelloDoc = new Document()
                .append("idScenario", indovinello.getIdScenario())
                .append("idScenarioRispGiusta", indovinello.getIdScenarioRispGiusta())
                .append("testoIndovinello", indovinello.getTestoIndovinello())
                .append("risposta", indovinello.getRisposta())
                .append("rispostaSbagliata", indovinello.getRispostaSbagliata())
                .append("idScenarioRispSbagliata", indovinello.getIdScenarioRispSbagliata());
        scenarioDoc.append("indovinello", indovinelloDoc);
    } else {
        scenarioDoc.append("indovinello", null);
    }

    // Creazione del documento principale per `Storia`
    Document document = new Document("_id", objectId)
            .append("titolo", storia.getTitolo())
            .append("inizio", scenarioDoc)
            .append("id", storia.getId())
            .append("username", storia.getUsername());

    System.out.println("SONO DENTROO A INSERIMENTO STORIE");
    storieCollection.insertOne(document);

    // Imposta l'ID della storia a partire dall'ObjectId generato
    storia.setId(objectId.hashCode());
    return storia;
}


    @Override
    public Scenario modificaScenario(int idScenario, int idStoria, String nuovoTesto) {
        Document query = new Document("_id", new ObjectId(String.valueOf(idStoria)))
                .append("scenari.id", idScenario);
        Document update = new Document("$set", new Document("scenari.$.testoScenario", nuovoTesto));
        storieCollection.updateOne(query, update);
        return getScenario(idScenario, idStoria);
    }

    @Override
    public Scenario getScenario(int idScenario, int idStoria) {
        Document query = new Document("_id", new ObjectId(String.valueOf(idStoria)))
                .append("scenari.id", idScenario);
        Document result = storieCollection.find(query).first();
        if (result != null) {
            List<Document> scenari = result.getList("scenari", Document.class);
            for (Document scenarioDoc : scenari) {
                if (scenarioDoc.getInteger("idScenario") == idScenario) {
                    return new Scenario(
                            result.getInteger("idStoria"),
                            scenarioDoc.getInteger("idScenario"),
                            scenarioDoc.getString("testoScenario"),
                            scenarioDoc.getString("oggetto"),
                            scenarioDoc.getList("alternative", Alternativa.class),
                            scenarioDoc.get("indovinello", Indovinello.class)
                    );
                }
            }
        }
        return null;
    }

    @Override
    public boolean inserisciScenario(Scenario scenario) {
        ObjectId objectId = new ObjectId();
        Document document = new Document("idScenario", objectId.hashCode())
                .append("testoScenario", scenario.getTestoScenario())
                .append("oggetto", scenario.getOggetto())
                .append("alternative", scenario.getAlternative())
                .append("indovinello", scenario.getIndovinello());
        Document update = new Document("$push", new Document("scenari", document));
        storieCollection.updateOne(new Document(), update);
        return true;
    }

    @Override
    public ArrayList<Scenario> getScenariStoria(int idStoria) {
        Document query = new Document("_id", new ObjectId(String.valueOf(idStoria)));
        Document result = storieCollection.find(query).first();
        if (result != null) {
            List<Document> scenariDocs = result.getList("scenari", Document.class);
            ArrayList<Scenario> scenari = new ArrayList<>();
            for (Document scenarioDoc : scenariDocs) {
                Scenario scenario = new Scenario(
                        result.getInteger("idStoria"),
                        scenarioDoc.getInteger("idScenario"),
                        scenarioDoc.getString("testoScenario"),
                        scenarioDoc.getString("oggetto"),
                        scenarioDoc.getList("alternative", Alternativa.class),
                        scenarioDoc.get("indovinello", Indovinello.class)
                );
                scenari.add(scenario);
            }
            return scenari;
        }
        return new ArrayList<>();
    }

    public void updateStoria(Storia storia) {
        Document query = new Document("_id", new ObjectId(String.valueOf(storia.getId())));
        Document update = new Document("$set", new Document("titolo", storia.getTitolo())
                .append("username", storia.getUsername())
                .append("inizio", storia.getInizio()));
        storieCollection.updateOne(query, update);
    }

    public void deleteStoria(int id) {
        Document query = new Document("_id", new ObjectId(String.valueOf(id)));
        storieCollection.deleteOne(query);
    }
}