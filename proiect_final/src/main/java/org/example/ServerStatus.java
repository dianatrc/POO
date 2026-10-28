package org.example;

public enum ServerStatus {
    UP("UP"),
    DOWN("DOWN"),
    DEGRADED("DEGRADED");

    private final String value;

    ServerStatus(String value) {
        this.value = value;
    }

    public String getValue() { return value; }
}
