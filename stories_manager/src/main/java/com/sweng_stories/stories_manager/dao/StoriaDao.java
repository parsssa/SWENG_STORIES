package com.sweng_stories.stories_manager.dao;

import com.mongodb.client.FindIterable;
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
    private final MongoCollection<Document> scenariCollection;

    @Autowired
    public StoriaDao(MongoDatabase database) {
        this.storieCollection = database.getCollection("storie");
        this.scenariCollection = database.getCollection("scenari");
    }

    @Override
    public Storia getStoriaConID(int idStoria) {
        // Esegui la query con il campo `id` come intero
        Document query = new Document("id", idStoria);
        Document result = storieCollection.find(query).first();

        if (result != null) {
            // Crea un oggetto Storia con titolo e username
            Storia storia = new Storia(
                    result.getString("titolo"),
                    result.getString("username"));

            // Imposta l'ID dal campo `id` nel documento
            storia.setId(result.getInteger("id"));

            // Conversione del campo "inizio" in un oggetto Scenario
            Document inizioDoc = result.get("inizio", Document.class);
            if (inizioDoc != null) {
                Scenario inizioScenario = new Scenario();
                inizioScenario.setIdStoria(inizioDoc.getInteger("idStoria"));
                inizioScenario.setIdScenario(inizioDoc.getInteger("idScenario"));
                inizioScenario.setTestoScenario(inizioDoc.getString("testoScenario"));
                inizioScenario.setOggetto(inizioDoc.getString("oggetto"));

                // Conversione della lista di alternative
                List<Document> alternativeDocs = (List<Document>) inizioDoc.get("alternative");
                List<Alternativa> alternative = new ArrayList<>();
                if (alternativeDocs != null) {
                    for (Document altDoc : alternativeDocs) {
                        Alternativa alternativa = new Alternativa();
                        alternativa.setIdScenario(altDoc.getInteger("idScenario"));
                        alternativa.setIdScenarioSuccessivo(altDoc.getInteger("idScenarioSuccessivo"));
                        alternativa.setTestoAlternativa(altDoc.getString("testoAlternativa"));
                        alternativa.setOggettoRichiesto(altDoc.getString("oggettoRichiesto"));
                        alternative.add(alternativa);
                    }
                }
                inizioScenario.setAlternative(alternative);

                // Conversione del campo indovinello
                Document indovinelloDoc = inizioDoc.get("indovinello", Document.class);
                if (indovinelloDoc != null) {
                    Indovinello indovinello = new Indovinello();
                    indovinello.setIdScenario(indovinelloDoc.getInteger("idScenario"));
                    indovinello.setIdScenarioRispGiusta(indovinelloDoc.getInteger("idScenarioRispGiusta"));
                    indovinello.setTestoIndovinello(indovinelloDoc.getString("testoIndovinello"));
                    indovinello.setRisposta(indovinelloDoc.getString("risposta"));
                    indovinello.setRispostaSbagliata(indovinelloDoc.getString("rispostaSbagliata"));
                    indovinello.setIdScenarioRispSbagliata(indovinelloDoc.getInteger("idScenarioRispSbagliata"));
                    inizioScenario.setIndovinello(indovinello);
                }

                // Imposta lo scenario iniziale nella storia
                storia.setInizio(inizioScenario);
            }

            System.out.println(storia);
            return storia;
        }
        return null;
    }

    @Override
    public ArrayList<Storia> getAllStorie() {
        List<Document> results = storieCollection.find().into(new ArrayList<>());
        ArrayList<Storia> storie = new ArrayList<>();

        for (Document result : results) {
            // Crea un oggetto Storia con titolo e username
            Storia storia = new Storia(
                    result.getString("titolo"),
                    result.getString("username"));

            // Imposta l'ID dal campo `id` nel documento
            storia.setId(result.getInteger("id"));

            // Conversione del campo "inizio" in un oggetto Scenario
            Document inizioDoc = result.get("inizio", Document.class);
            if (inizioDoc != null) {
                Scenario inizioScenario = new Scenario();
                inizioScenario.setIdStoria(inizioDoc.getInteger("idStoria"));
                inizioScenario.setIdScenario(inizioDoc.getInteger("idScenario"));
                inizioScenario.setTestoScenario(inizioDoc.getString("testoScenario"));
                inizioScenario.setOggetto(inizioDoc.getString("oggetto"));

                // Conversione della lista di alternative
                List<Document> alternativeDocs = (List<Document>) inizioDoc.get("alternative");
                List<Alternativa> alternative = new ArrayList<>();
                if (alternativeDocs != null) {
                    for (Document altDoc : alternativeDocs) {
                        Alternativa alternativa = new Alternativa();
                        alternativa.setIdScenario(altDoc.getInteger("idScenario"));
                        alternativa.setIdScenarioSuccessivo(altDoc.getInteger("idScenarioSuccessivo"));
                        alternativa.setTestoAlternativa(altDoc.getString("testoAlternativa"));
                        alternativa.setOggettoRichiesto(altDoc.getString("oggettoRichiesto"));
                        alternative.add(alternativa);
                    }
                }
                inizioScenario.setAlternative(alternative);

                // Conversione del campo indovinello
                Document indovinelloDoc = inizioDoc.get("indovinello", Document.class);
                if (indovinelloDoc != null) {
                    Indovinello indovinello = new Indovinello();
                    indovinello.setIdScenario(indovinelloDoc.getInteger("idScenario"));
                    indovinello.setIdScenarioRispGiusta(indovinelloDoc.getInteger("idScenarioRispGiusta"));
                    indovinello.setTestoIndovinello(indovinelloDoc.getString("testoIndovinello"));
                    indovinello.setRisposta(indovinelloDoc.getString("risposta"));
                    indovinello.setRispostaSbagliata(indovinelloDoc.getString("rispostaSbagliata"));
                    indovinello.setIdScenarioRispSbagliata(indovinelloDoc.getInteger("idScenarioRispSbagliata"));
                    inizioScenario.setIndovinello(indovinello);
                }

                // Imposta lo scenario iniziale nella storia
                storia.setInizio(inizioScenario);
            }

            // Aggiungi la storia alla lista
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
                    result.getString("username"));
            storia.setId(result.getObjectId("_id").hashCode());
            storia.setInizio(result.get("inizio", Scenario.class));
            storie.add(storia);
        }
        return storie;
    }

    @Override
    public Storia inserisciStoria(Storia storia) {
        ObjectId objectId = new ObjectId();
        storia.setId(objectId.hashCode());


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

        storieCollection.insertOne(document);

        // // Imposta l'ID della storia a partire dall'ObjectId generato
        // storia.setId(objectId.hashCode());
        System.out.println("ID finale della storia: " + storia.getId());
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
    public Scenario getScenario(int idStoria, int idScenario) {
        System.out.println("Cercando scenario con idStoria: " + idStoria + ", idScenario: " + idScenario);
    
        Document query = new Document("idStoria", idStoria)
                .append("idScenario", idScenario);
        System.out.println("Query generata: " + query.toJson());
    
        Document scenarioDoc = scenariCollection.find(query).first();
        System.out.println("Risultato della query: " + scenarioDoc);
    
        if (scenarioDoc != null) {
            // Conversione della lista `alternative` da Document a Alternativa
            List<Document> alternativeDocs = (List<Document>) scenarioDoc.get("alternative");
            List<Alternativa> alternativeList = new ArrayList<>();
            if (alternativeDocs != null) {
                for (Document altDoc : alternativeDocs) {
                    Alternativa alternativa = new Alternativa(
                            altDoc.getInteger("idScenario"),
                            altDoc.getInteger("idScenarioSuccessivo"),
                            altDoc.getString("testoAlternativa"),
                            altDoc.getString("oggettoRichiesto")
                    );
                    alternativeList.add(alternativa);
                }
            }
    
            // Conversione dell’indovinello, se presente
            Document indovinelloDoc = (Document) scenarioDoc.get("indovinello");
            Indovinello indovinello = null;
            if (indovinelloDoc != null) {
                indovinello = new Indovinello(
                        indovinelloDoc.getInteger("idScenario"),
                        indovinelloDoc.getInteger("idScenarioRispGiusta"),
                        indovinelloDoc.getString("testoIndovinello"),
                        indovinelloDoc.getString("risposta"),
                        indovinelloDoc.getString("rispostaSbagliata"),
                        indovinelloDoc.getInteger("idScenarioRispSbagliata")
                );
            }
    
            Scenario scenario = new Scenario(
                    scenarioDoc.getInteger("idStoria"),
                    scenarioDoc.getInteger("idScenario"),
                    scenarioDoc.getString("testoScenario"),
                    scenarioDoc.getString("oggetto"),
                    alternativeList,
                    indovinello
            );
            
            System.out.println("Scenario trovato: " + scenario);
            return scenario;
        }
    
        System.out.println("Nessuno scenario trovato per la query specificata.");
        return null;
    }
            
    @Override
    public boolean inserisciScenario(Scenario scenario) {
        ObjectId objectId = new ObjectId();

        // Creazione del documento per l'oggetto `Scenario`
        Document scenarioDoc = new Document()
                .append("idStoria", scenario.getIdStoria())
                .append("idScenario", scenario.getIdScenario())
                .append("testoScenario", scenario.getTestoScenario())
                .append("oggetto", scenario.getOggetto());

        // Creazione della lista di `Alternativa` come documenti
        List<Document> alternativeDocs = new ArrayList<>();

        if (scenario.getAlternative() != null) {
            for (Alternativa alternativa : scenario.getAlternative()) {
                Document alternativaDoc = new Document()
                        .append("idScenario", alternativa.getIdScenario())
                        .append("idScenarioSuccessivo", alternativa.getIdScenarioSuccessivo())
                        .append("testoAlternativa", alternativa.getTestoAlternativa())
                        .append("oggettoRichiesto", alternativa.getOggettoRichiesto());
                alternativeDocs.add(alternativaDoc);
            }
            scenarioDoc.append("alternative", alternativeDocs);
        }

        // Conversione di Indovinello in Document, se presente
        if (scenario.getIndovinello() != null) {
            Indovinello indovinello = scenario.getIndovinello();
            Document indovinelloDoc = new Document("idScenario", indovinello.getIdScenario())
                    .append("idScenarioRispGiusta", indovinello.getIdScenarioRispGiusta())
                    .append("testoIndovinello", indovinello.getTestoIndovinello())
                    .append("risposta", indovinello.getRisposta())
                    .append("rispostaSbagliata", indovinello.getRispostaSbagliata())
                    .append("idScenarioRispSbagliata", indovinello.getIdScenarioRispSbagliata());
            scenarioDoc.append("indovinello", indovinelloDoc);
        }

        // Inserimento dello scenario nella collezione `scenari`
        scenariCollection.insertOne(scenarioDoc);

        return true;
    }

    @Override
    public ArrayList<Scenario> getScenariStoria(int idStoria) {
        // Creiamo la query per cercare gli scenari con il campo "idStoria" specificato
        Document query = new Document("idStoria", idStoria);

        // Eseguiamo la ricerca nella collezione "scenariocollection"
        FindIterable<Document> scenariDocs = scenariCollection.find(query);

        // Inizializziamo la lista degli scenari da restituire
        ArrayList<Scenario> scenari = new ArrayList<>();

        // Iteriamo sui documenti trovati e li convertiamo in oggetti Scenario
        for (Document scenarioDoc : scenariDocs) {
            Scenario scenario = new Scenario(
                    scenarioDoc.getInteger("idStoria"),
                    scenarioDoc.getInteger("idScenario"),
                    scenarioDoc.getString("testoScenario"),
                    scenarioDoc.getString("oggetto"),
                    scenarioDoc.getList("alternative", Alternativa.class),
                    scenarioDoc.get("indovinello", Indovinello.class));
            scenari.add(scenario);
        }

        return scenari; // Restituiamo la lista di scenari trovati
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