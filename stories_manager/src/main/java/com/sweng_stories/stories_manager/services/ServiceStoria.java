package com.sweng_stories.stories_manager.services;

import com.sweng_stories.stories_manager.dao.OpStoriaDao;
import com.sweng_stories.stories_manager.domain.Scenario;
import com.sweng_stories.stories_manager.domain.Storia;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
@Service
public class ServiceStoria implements OpStoria {

    @Autowired
    OpStoriaDao storiaDao;

    @Override
    public Storia getStoriaConID(int idStoria) {
        return storiaDao.getStoriaConID(idStoria);
    }

    @Override
    public ArrayList<Storia> getAllStorie() {
        return storiaDao.getAllStorie();
    }

    @Override
    public ArrayList<Storia> getStoriaConUsername(String username) {
        return storiaDao.getStoriaConUsername(username);
    }

    @Override
    public Storia inserisciStoria(Storia storia) {
        return storiaDao.inserisciStoria(storia);
    }

    @Override
    public Scenario modificaScenario(int idScenario, int idStoria, String nuovoTesto) {
        return storiaDao.modificaScenario(idScenario,idStoria,nuovoTesto);
    }

    @Override
    public Scenario getScenario(int idScenario, int idStoria) {
        return storiaDao.getScenario(idStoria,idScenario);
    }

    @Override
    public boolean inserisciScenario(Scenario scenario) {
        return storiaDao.inserisciScenario(scenario);
    }

    @Override
    public ArrayList<Scenario> getScenariStoria(int idStoria) {
        return storiaDao.getScenariStoria(idStoria);
    }
}
