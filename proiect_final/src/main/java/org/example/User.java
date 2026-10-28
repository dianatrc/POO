package org.example;

public class User {
    protected final String name;
    protected final String role;
    protected final String email;

    public User(String name, String role, String email) {
        this.name = name;
        this.role = role;
        this.email = email;
    }

    public String getName() { return name; }
    public String getRole() { return role; }
    public String getEmail() { return email; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        return email.equals(((User) o).email); // este unic dupa email
    }

    @Override
    public int hashCode() {
        return email.hashCode();
    }
}
