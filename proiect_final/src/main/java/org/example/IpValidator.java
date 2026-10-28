package org.example;

import java.util.List;

public class IpValidator extends BaseValidator {
    @Override
    public void validate(List<String> data)
        throws MissingIpAddressException, LocationException, UserException {
        if (data == null || data.isEmpty()) return;
        String command = data.get(0);
        int ipIndex = 1;
        if (command.equals("ADD SERVER"))
            ipIndex = 2;
        if (data.size() <= ipIndex || data.get(ipIndex).isEmpty())
            throw new MissingIpAddressException();

        checkNext(data);
    }
}
