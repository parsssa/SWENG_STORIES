package com.sweng_stories.stories_manager;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sweng_stories.stories_manager.controller.JsonController;
import com.sweng_stories.stories_manager.domain.Utente;

public class JsonControllerTest {

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private JsonController jsonController;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCreateUtente() throws IOException {
        // Mock dei dati
        Utente utente = new Utente();
        utente.setUsername("testuser");
        utente.setPassword("testpassword");

        // Test della creazione dell'utente
        Utente createdUtente = jsonController.createUtente(utente);

        // Verifica se l'utente è stato creato correttamente
        assertNotNull(createdUtente);
        assertTrue(createdUtente.getUsername().equals("testuser"));
        assertTrue(createdUtente.getPassword().equals("testpassword"));

        // // Test di eliminazione dell'utente appena creato
        // jsonController.deleteUtente("testuser");
        // Utente deletedUtente = jsonController.getUtenteByUsername("testuser");

        // // Verifica che l'utente sia stato eliminato correttamente
        // assertNull(deletedUtente);
    }
}


