package org.example;

public interface Observer {
    void update(String ip, AlertType type, Severity severity, String message);
}
