package com.sweng_stories.stories_manager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.util.stream.Collectors;

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
            storia.setDescrizione(document.getString("descrizione"));
            storia.setInizio(convertDocumentToScenario(document.get("inizio", Document.class)));
            storia.setFinali(convertDocumentsToScenarios((List<Document>) document.get("finali")));
            storia.setScenari(convertDocumentsToScenarios((List<Document>) document.get("scenari")));
            storia.setIndovinello(convertDocumentToIndovinello(document.get("indovinello", Document.class)));
            storia.setInventario(convertDocumentToInventario(document.get("inventario", Document.class)));
            storie.add(storia);
        });
        return storie;
    }

    public List<String> getAllStorieTitoli() {
        List<String> titoli = new ArrayList<>();
        storieCollection.find().forEach(document -> {
            String titolo = document.getString("titolo");
            if (titolo != null) {
                titoli.add(titolo);
            }
        });
        return titoli;
    }

    public Storia getStoriaById(Long id) {
        Document query = new Document("id", id);
        Document result = storieCollection.find(query).first();
        if (result != null) {
            Storia storia = new Storia();
            storia.setId(result.getLong("id"));
            storia.setTitolo(result.getString("titolo"));
            storia.setDescrizione(result.getString("descrizione"));
            storia.setInizio(convertDocumentToScenario(result.get("inizio", Document.class)));
            storia.setFinali(convertDocumentsToScenarios((List<Document>) result.get("finali")));
            storia.setScenari(convertDocumentsToScenarios((List<Document>) result.get("scenari")));
            storia.setIndovinello(convertDocumentToIndovinello(result.get("indovinello", Document.class)));
            storia.setInventario(convertDocumentToInventario(result.get("inventario", Document.class)));
            return storia;
        }
        return null;
    }
    

    private Long generateId(String collectionName) {
        Long id = 1L;
        while (documentExists(collectionName, id)) {
            id++;
        }
        return id;
    }

    private boolean documentExists(String collectionName, Long id) {
        MongoCollection<Document> collection = database.getCollection(collectionName);
        Document query = new Document("id", id);
        return collection.find(query).first() != null;
    }

    public Storia createStoria(Storia nuovaStoria) {
        Long newId = generateId("storie");
        nuovaStoria.setId(newId);
    
        // Assicura che le liste non siano null
        if (nuovaStoria.getInizio() != null && nuovaStoria.getInizio().getIndovinelli() == null) {
            nuovaStoria.getInizio().setIndovinelli(new ArrayList<>());
        }
    
        if (nuovaStoria.getFinali() == null) {
            nuovaStoria.setFinali(new ArrayList<>());
        } else {
            for (Scenario finale : nuovaStoria.getFinali()) {
                if (finale.getIndovinelli() == null) {
                    finale.setIndovinelli(new ArrayList<>());
                }
            }
        }
    
        if (nuovaStoria.getScenari() == null) {
            nuovaStoria.setScenari(new ArrayList<>());
        } else {
            for (Scenario scenario : nuovaStoria.getScenari()) {
                if (scenario.getIndovinelli() == null) {
                    scenario.setIndovinelli(new ArrayList<>());
                }
            }
        }
    
        // Conversione e inserimento nel database
        Document document = new Document("id", nuovaStoria.getId())
                .append("titolo", nuovaStoria.getTitolo())
                .append("descrizione", nuovaStoria.getDescrizione())
                .append("inizio", convertScenarioToDocument(nuovaStoria.getInizio()))
                .append("finali", convertScenariosToDocuments(nuovaStoria.getFinali()))
                .append("scenari", convertScenariosToDocuments(nuovaStoria.getScenari()))
                .append("indovinello", convertIndovinelloToDocument(nuovaStoria.getIndovinello()))
                .append("inventario", convertInventarioToDocument(nuovaStoria.getInventario()));
    
        storieCollection.insertOne(document);
        return nuovaStoria;
    }
    

    public Storia updateStoria(Long id, Storia storiaAggiornata) {
        Document query = new Document("id", id);
        Document update = new Document("$set", new Document("titolo", storiaAggiornata.getTitolo())
                .append("descrizione", storiaAggiornata.getDescrizione())
                .append("inizio", convertScenarioToDocument(storiaAggiornata.getInizio()))
                .append("finali", convertScenariosToDocuments(storiaAggiornata.getFinali()))
                .append("scenari", convertScenariosToDocuments(storiaAggiornata.getScenari()))
                .append("indovinello", convertIndovinelloToDocument(storiaAggiornata.getIndovinello()))
                .append("inventario", convertInventarioToDocument(storiaAggiornata.getInventario())));
        storieCollection.updateOne(query, update);
        return storiaAggiornata;
    }
    

    private Document convertOggettoToDocument(Oggetto oggetto) {
        return new Document("id", oggetto.getId())
                .append("nome", oggetto.getNome())
                .append("descrizione", oggetto.getDescrizione());
    }
    

    private Document convertScenarioToDocument(Scenario scenario) {
        return new Document("id", scenario.getId())
                .append("descrizione", scenario.getDescrizione())
                .append("indovinelli", scenario.getIndovinelli().stream()
                        .map(this::convertIndovinelloToDocument).collect(Collectors.toList()))
                .append("oggetti", scenario.getOggetti().stream()
                        .map(this::convertOggettoToDocument).collect(Collectors.toList()))
                .append("alternative", convertAlternativesToDocuments(scenario.getAlternatives()));  // Aggiungi le alternative
    }

    private Document convertAlternativeToDocument(Alternative alternative) {
        return new Document("text", alternative.getText())
                .append("type", alternative.getType())
                .append("items", alternative.getItems());
    }
    
    private List<Document> convertAlternativesToDocuments(List<Alternative> alternatives) {
        return alternatives.stream()
                .map(this::convertAlternativeToDocument)
                .collect(Collectors.toList());
    }
    
    private Alternative convertDocumentToAlternative(Document document) {
        return new Alternative(
                document.getString("text"),
                document.getString("type"),
                (List<String>) document.get("items")
        );
    }
    
    private List<Alternative> convertDocumentsToAlternatives(List<Document> documents) {
        return documents.stream()
                .map(this::convertDocumentToAlternative)
                .collect(Collectors.toList());
    }
    
    
    

    private List<Document> convertScenariosToDocuments(List<Scenario> scenari) {
        List<Document> documents = new ArrayList<>();
        for (Scenario scenario : scenari) {
            documents.add(convertScenarioToDocument(scenario));
        }
        return documents;
    }

    private Document convertIndovinelloToDocument(Indovinello indovinello) {
        Document doc = new Document("id", indovinello.getId())
                .append("descrizione", indovinello.getDescrizione())
                .append("domanda", indovinello.getDomanda())
                .append("scenarioId", indovinello.getScenarioId());
    
        if (indovinello instanceof IndovinelloTestuale) {
            doc.append("tipo", "testuale")
               .append("rispostaCorretta", indovinello.getRispostaCorretta());
        } else if (indovinello instanceof IndovinelloNumerico) {
            doc.append("tipo", "numerico")
               .append("rispostaCorretta", indovinello.getRispostaCorretta());
        }
    
        return doc;
    }

    private List<Document> convertIndovinelliToDocuments(List<Indovinello> indovinelli) {
        if (indovinelli == null) {
            indovinelli = new ArrayList<>();
        }
        List<Document> documents = new ArrayList<>();
        for (Indovinello indovinello : indovinelli) {
            documents.add(convertIndovinelloToDocument(indovinello));
        }
        return documents;
    }

    private Document convertInventarioToDocument(Inventario inventario) {
        return new Document("id", inventario.getId())
                .append("oggetti", convertOggettiToDocuments(inventario.getOggetti()));
    }

    private List<Document> convertOggettiToDocuments(List<Oggetto> oggetti) {
        List<Document> documents = new ArrayList<>();
        for (Oggetto oggetto : oggetti) {
            documents.add(new Document("id", oggetto.getId())
                    .append("nome", oggetto.getNome())
                    .append("descrizione", oggetto.getDescrizione()));
        }
        return documents;
    }

    private Scenario convertDocumentToScenario(Document document) {
        if (document == null) {
            return null;
        }
        Scenario scenario = new Scenario();
        scenario.setId(document.getLong("id"));
        scenario.setDescrizione(document.getString("descrizione"));
        scenario.setIndovinelli(convertDocumentsToIndovinelli((List<Document>) document.get("indovinelli")));
        scenario.setOggetti(convertDocumentsToOggetti((List<Document>) document.get("oggetti")));
        scenario.setAlternatives(convertDocumentsToAlternatives((List<Document>) document.get("alternative")));  // Gestisci le alternative
        return scenario;
    }
    
    

    private List<Indovinello> convertDocumentsToIndovinelli(List<Document> documents) {

        System.out.println("INDOVINELLOOOOOO: \n \n \n \n \n \n \n \n" + " tipo: " + " ds \n \n \n \n \n \n \n \n");

        List<Indovinello> indovinelli = new ArrayList<>();
        for (Document document : documents) {
            indovinelli.add(convertDocumentToIndovinello(document));
        }
        return indovinelli;
    }

    private List<Scenario> convertDocumentsToScenarios(List<Document> documents) {
        List<Scenario> scenari = new ArrayList<>();
        for (Document document : documents) {
            scenari.add(convertDocumentToScenario(document));
        }
        return scenari;
    }

    private Indovinello convertDocumentToIndovinello(Document document) {
        if (document == null) {
            return null;
        }
    
        String tipo = document.getString("tipo");
        Long id = document.getLong("id");
        String descrizione = document.getString("descrizione");
        String domanda = document.getString("domanda");
        Object rispostaCorretta = document.get("rispostaCorretta");
        Long scenarioId = document.getLong("scenarioId");
    
        if ("testuale".equals(tipo)) {
            return new IndovinelloTestuale(id, descrizione, domanda, (String) rispostaCorretta, scenarioId);
        } else if ("numerico".equals(tipo)) {
            return new IndovinelloNumerico(id, descrizione, domanda, (Integer) rispostaCorretta, scenarioId);
        } else {
            return null;
        }
    }
    

    private Inventario convertDocumentToInventario(Document document) {
        if (document == null) {
            return null;
        }
        Inventario inventario = new Inventario();
        inventario.setId(document.getLong("id"));
        inventario.setOggetti(convertDocumentsToOggetti((List<Document>) document.get("oggetti")));
        return inventario;
    }

    private List<Oggetto> convertDocumentsToOggetti(List<Document> documents) {
        List<Oggetto> oggetti = new ArrayList<>();
        for (Document document : documents) {
            Oggetto oggetto = new Oggetto();
            oggetto.setId(document.getLong("id"));
            oggetto.setNome(document.getString("nome"));
            oggetto.setDescrizione(document.getString("descrizione"));
            oggetti.add(oggetto);
        }
        return oggetti;
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

    // Metodi per Indovinello
    public List<Indovinello> getIndovinelliByScenarioId(Long scenarioId) {
        List<Indovinello> indovinelli = new ArrayList<>();
        Document query = new Document("scenarioId", scenarioId);
        indovinelliCollection.find(query).forEach(document -> {
            String tipo = document.getString("tipo");
            Long id = document.getLong("id");
            String descrizione = document.getString("descrizione");
            String domanda = document.getString("domanda");
            Object rispostaCorretta = document.get("rispostaCorretta");
            Indovinello indovinello;
            if ("testuale".equals(tipo)) {
                indovinello = new IndovinelloTestuale(id, descrizione, domanda, (String) rispostaCorretta, scenarioId);
            } else {
                indovinello = new IndovinelloNumerico(id, descrizione, domanda, (Integer) rispostaCorretta, scenarioId);
            }
            // Aggiungi altri campi se necessario
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
            String domanda = result.getString("domanda");
            Object rispostaCorretta = result.get("rispostaCorretta");
            if ("testuale".equals(tipo)) {
                return new IndovinelloTestuale(idResult, descrizione, domanda, (String) rispostaCorretta,
                        result.getLong("scenarioId"));
            } else if ("numerico".equals(tipo)) {
                return new IndovinelloNumerico(idResult, descrizione, domanda, (Integer) rispostaCorretta,
                        result.getLong("scenarioId"));
            } else {
                return null;
            }
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
        // Aggiungi altri campi se necessario
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
        // Aggiungi altri campi se necessario
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
