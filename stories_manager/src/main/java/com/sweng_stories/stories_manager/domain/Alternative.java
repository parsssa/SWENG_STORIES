// Alternative.java
package com.sweng_stories.stories_manager.domain;

import java.util.List;

public class Alternative {
    private String text;
    private String type; // "with-items" or "without-items"
    private List<String> items;

    public Alternative() {}

    public Alternative(String text, String type, List<String> items) {
        this.text = text;
        this.type = type;
        this.items = items;
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
}
