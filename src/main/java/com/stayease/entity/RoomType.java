package com.stayease.entity;

public enum RoomType {
    STANDARD("Standard Room"),
    DELUXE("Deluxe King Room"),
    SUITE("Executive Suite"),
    FAMILY_SUITE("Family Grand Suite"),
    PRESIDENTIAL("Presidential Luxury Suite");

    private final String displayName;

    RoomType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
