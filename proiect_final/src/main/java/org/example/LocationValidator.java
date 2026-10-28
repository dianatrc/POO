package org.example;

import java.util.List;

public class LocationValidator extends BaseValidator {
    @Override
    public void validate(List<String> data)
            throws MissingIpAddressException, LocationException, UserException {
        if (data == null || data.isEmpty()) return;
        if (data.get(0).equals("ADD SERVER")) {
            if (data.size() < 5 || data.get(4).isEmpty()) {
                throw new LocationException();
            }
        }

        checkNext(data);
    }
}
