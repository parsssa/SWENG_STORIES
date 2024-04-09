// MongoDbControllerTest.java

package com.sweng_stories.stories_manager;

import com.sweng_stories.stories_manager.controller.MongoDbController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class MongoDbControllerTest {

    @Autowired
    private MongoDbController mongoDbController;

    @Test
    public void testWriteToDatabase() {
        // Scriviamo un documento di test sul database
        String collectionName = "testCollection";
        String document = "{ \"name\": \"John\", \"age\": 30 }";
        mongoDbController.writeToDatabase(collectionName, document);

        // Verifichiamo che il database sia stato creato e che contenga almeno un documento nella collezione specificata
        assertTrue(mongoDbController.getDatabase() != null, "MongoDB connection failed!");
        assertTrue(mongoDbController.getDatabase().getCollection(collectionName).countDocuments() > 0,
                "Document not written to database or collection does not exist!");
    }
}
