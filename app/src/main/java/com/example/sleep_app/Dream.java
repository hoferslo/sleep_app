package com.example.sleep_app;

import java.time.LocalDateTime;

public class Dream {
    private Long id;
    private String title;
    private int lucidity;
    private int clarity;
    private String feeling;
    private String description;
    private LocalDateTime dateCreated;

    public Dream(Long id, String title, int lucidity, int clarity, String feeling, String description, LocalDateTime dateCreated) {
        this.id = id;
        this.title = title;
        this.lucidity = lucidity;
        this.clarity = clarity;
        this.feeling = feeling;
        this.description = description;
        this.dateCreated = dateCreated;
    }

    public Dream() {

    }

    public enum Attribute {
        _id, title, lucidity, clarity, feeling, description, date_created
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public int getLucidity() {
        return lucidity;
    }

    public int getClarity() {
        return clarity;
    }

    public String getFeeling() {
        return feeling;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getDateCreated() {
        return dateCreated;
    }
}
