package org.example;

public enum AlertType {
    ANOMALY("ANOMALY"),
    ADVISORY("ADVISORY");

    private final String value;

    AlertType(String value) {
        this.value = value;
    }

    public String getValue() { return value; }
}
