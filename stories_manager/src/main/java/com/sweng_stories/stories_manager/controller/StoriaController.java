package com.sweng_stories.stories_manager.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sweng_stories.stories_manager.domain.Storia;

@RestController
@RequestMapping("/api/storie")
public class StoriaController {

    @Autowired
    private MongoDbController mongoDbController;

    @GetMapping("/titoli")
    public ResponseEntity<List<StoriaTitoloDto>> getAllStorieTitoli() {
        List<Storia> storie = mongoDbController.getAllStorie();
        List<StoriaTitoloDto> storieTitoli = storie.stream()
                .map(storia -> new StoriaTitoloDto(storia.getId(), storia.getTitolo()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(storieTitoli);
    }

    // Classe DTO per restituire solo ID e titolo della storia
    public static class StoriaTitoloDto {
        private Long id;
        private String titolo;

        public StoriaTitoloDto(Long id, String titolo) {
            this.id = id;
            this.titolo = titolo;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getTitolo() {
            return titolo;
        }

        public void setTitolo(String titolo) {
            this.titolo = titolo;
        }
    }
}
