package org.example;

public enum Severity {
    LOW("LOW"),
    MEDIUM("MEDIUM"),
    HIGH("HIGH"),
    CRITICAL("CRITICAL");

    private final String value;

    Severity(String value) {
        this.value = value;
    }

    public String getValue() { return value; }
}
