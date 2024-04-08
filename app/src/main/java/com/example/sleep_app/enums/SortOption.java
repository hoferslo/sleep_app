package com.example.sleep_app.enums;

public enum SortOption {
    LUCIDITY("Lucidity"),
    CLARITY("Clarity"),
    DATE("Date"),
    ALPHABETICALLY("Alphabetically");

    private final String title;

    SortOption(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
