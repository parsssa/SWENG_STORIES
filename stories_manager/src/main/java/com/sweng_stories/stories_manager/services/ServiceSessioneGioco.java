package com.sweng_stories.stories_manager.services;

import com.sweng_stories.stories_manager.dao.OpSessioneGiocoDao;
import com.sweng_stories.stories_manager.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class ServiceSessioneGioco implements OpSessioneGioco {
    @Autowired
    OpSessioneGiocoDao sessioneGiocoDao;
    @Override
    public Scenario elaboraIndovinello(int idScenario, String risposta, int idPartita) {
        SessioneGioco partita = sessioneGiocoDao.getSessioneConID(idPartita);

        if(partita == null){
            return null;
        }

        int idStoria = partita.getIdStoria();

        Scenario scenario = sessioneGiocoDao.getScenarioCorrente(idStoria,idScenario);
        Indovinello indovinello = scenario.getIndovinello();

        if(indovinello == null)
            return null;

        boolean esito = indovinello.getRisposta().equals(risposta);

        if(esito){
            int idScenarioSuccessivo = indovinello.getIdScenarioRispGiusta(); //dipende da getRispostaCorretta.scenarioId

            Scenario scenarioSuccessivo = sessioneGiocoDao.getScenarioCorrente(idStoria,idScenarioSuccessivo);
            partita.setIdScenarioCorrente(scenarioSuccessivo.getIdScenario());
            sessioneGiocoDao.aggiornaSessione(partita);

            return scenarioSuccessivo;
        }else{
            int idScenarioErrato = indovinello.getIdScenarioRispSbagliata(); //dipende da getRispostaSbagliata.scenarioId da correggere

            Scenario scenarioSuccessivo = sessioneGiocoDao.getScenarioCorrente(idStoria,idScenarioErrato);
            partita.setIdScenarioCorrente(scenarioSuccessivo.getIdScenario());
            sessioneGiocoDao.aggiornaSessione(partita);

            return scenarioSuccessivo;
        }
    }

    @Override
    public Scenario elaboraAlternativa(int idScenario,
                                       int idScelta, int idPartita) {
        SessioneGioco partita = sessioneGiocoDao.getSessioneConID(idPartita);

        if(partita == null)
            return null;

        int idStoria = partita.getIdStoria();

        Scenario scenario = sessioneGiocoDao.getScenarioCorrente(idStoria,idScenario);

        List<Alternativa> decisioni = scenario.getAlternative();

        if(idScelta > decisioni.size()-1 || idScelta < 0){
            return null;
        }

        Alternativa alternativa = scenario.getAlternative().get(idScelta);
        String oggettoNecessario = alternativa.getOggettoRichiesto();

        if(!oggettoNecessario.isEmpty()){
            Inventario inventario = partita.getInventario();
            if(!inventario.getOggetti().contains(oggettoNecessario))
                return null;
        }

        int idScenarioSuccessivo = alternativa.getIdScenarioSuccessivo();

        Scenario scenarioSuccessivo = sessioneGiocoDao.getScenarioCorrente(idStoria,idScenarioSuccessivo);
        partita.setIdScenarioCorrente(scenarioSuccessivo.getIdScenario());
        sessioneGiocoDao.aggiornaSessione(partita);

        return scenarioSuccessivo;
    }

    @Override
    public Inventario raccogliOggetto(int idPartita, String oggetto) {
        SessioneGioco partita = sessioneGiocoDao.getSessioneConID(idPartita);

        if(partita == null)
            return null;

        partita.getInventario().raccogliOggetto(oggetto);

        boolean aggiornamento = sessioneGiocoDao.aggiornaSessione(partita);

        if(!aggiornamento)
            return null;

        return partita.getInventario();

    }

    @Override
    public SessioneGioco creaSessione(SessioneGioco partita) {
        return sessioneGiocoDao.creaSessione(partita);
    }

    @Override
    public boolean eliminaSessione(int idPartita) {
        return sessioneGiocoDao.eliminaSessione(idPartita);
    }

    @Override
    public ArrayList<SessioneGioco> getSessioniUtente(String username) {
        return sessioneGiocoDao.getSessioniUtente(username);
    }

    @Override
    public SessioneGioco getSessioneConID(int idPartita) {
        return sessioneGiocoDao.getSessioneConID(idPartita);
    }
}
