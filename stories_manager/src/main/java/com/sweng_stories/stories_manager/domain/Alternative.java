// Alternative.java
package com.sweng_stories.stories_manager.domain;

import java.util.List;

public class Alternative {
    private String text;
    private String type; // "with-items" or "without-items"
    private List<String> items;
    private Long nextScenarioId; // Aggiungi questo campo per collegare l'alternativa allo scenario

    public Alternative() {}

    public Alternative(String text, String type, List<String> items, Long nextScenarioId) {
        this.text = text;
        this.type = type;
        this.items = items;
        this.nextScenarioId = nextScenarioId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getItems() {
        return items;
    }

    public void setItems(List<String> items) {
        this.items = items;
    }

    public Long getNextScenarioId() {
        return nextScenarioId;
    }

    public void setNextScenarioId(Long nextScenarioId) {
        this.nextScenarioId = nextScenarioId;
    }

    @Override
    public String toString() {
        return "Alternative [text=" + text + ", type=" + type + ", items=" + items + ", nextScenarioId="
                + nextScenarioId + "]";
    }

    

    
}
