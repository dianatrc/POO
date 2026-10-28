package org.example;

import java.util.List;

public class UserValidator extends BaseValidator {
    @Override
    public void validate(List<String> data)
            throws MissingIpAddressException, LocationException, UserException {
        if (data == null || data.isEmpty()) return;

        String command = data.get(0);
        if (command.equals("ADD SERVER")) {
            if (data.size() < 11 || data.get(9).isEmpty() || data.get(10).isEmpty())
                throw new UserException();
        } else if (command.contains("MEMBER")) {
            // ADD, FIND sau REMOVE MEMBER
            if (data.size() < 4 || data.get(2).isEmpty() || data.get(3).isEmpty())
                throw new UserException();
        }
        checkNext(data);
    }
}
