package com.sweng_stories.stories_manager.restcontrollers;

import com.sweng_stories.stories_manager.domain.Scenario;
import com.sweng_stories.stories_manager.domain.Storia;
import com.sweng_stories.stories_manager.services.OpStoria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api")
public class StoriaController implements OpStoria {
    @Autowired
    OpStoria serviceStoria;
    @Override
    public Storia getStoriaConID(int idStoria) {
        return serviceStoria.getStoriaConID(idStoria);
    }

    @Override
    public ArrayList<Storia> getAllStorie() {
        return serviceStoria.getAllStorie();
    }

    @Override
    public ArrayList<Storia> getStoriaConUsername(String username) {
        return serviceStoria.getStoriaConUsername(username);
    }

    @Override
    public Storia inserisciStoria(Storia storia) {
        return serviceStoria.inserisciStoria(storia);
    }

    @Override
    public Scenario modificaScenario(int idScenario, int idStoria, String nuovoTesto) {
        return serviceStoria.modificaScenario(idScenario, idStoria, nuovoTesto);
    }

    @Override
    public Scenario getScenario(int idScenario, int idStoria) {
        return serviceStoria.getScenario(idScenario, idStoria);
    }

    @Override
    public boolean inserisciScenario(Scenario scenario) {
        return serviceStoria.inserisciScenario(scenario);
    }

    @Override
    public ArrayList<Scenario> getScenariStoria(int idStoria) {
        return serviceStoria.getScenariStoria(idStoria);
    }
}
