package org.example;

public class UserFactory {
    public static User createUser(String name, String role, String email, String department, String clearanceString) throws UserException {
        if (name.isEmpty() || role.isEmpty() || name == null || role == null)
            throw new UserException();

        switch (role.toLowerCase()) {
            case "admin" -> {
                int clearance = 0;
                if (!clearanceString.isEmpty())
                    clearance = Integer.parseInt(clearanceString);
                return new Admin(name, role, email, department, clearance);
            }
            case "operator" -> {
                return new Operator(name, role, email, department);
            }
            default -> {
                return new User(name, role, email);
            }
        }
    }
}
