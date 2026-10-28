package org.example;

public class MissingIpAddressException extends Exception {
    public MissingIpAddressException() {
        super("Server IP Address was not provided.");
    }

    @Override
    public String toString() {
        return "MissingIpAddressException: " + super.getMessage();
    }
}
