package org.example;

public class UserException extends Exception {
    public UserException() {
        super("Name and role can't be empty.");
    }

    @Override
    public String toString() {
        return "UserException: " + super.getMessage();
    }
}
