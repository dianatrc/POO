package org.example;

import java.util.List;

public abstract class BaseValidator {
    protected BaseValidator next;

    public void setNext(BaseValidator next) { this.next = next; }

    public abstract void validate(List<String> data) throws MissingIpAddressException, LocationException, UserException;

    protected void checkNext(List<String> data)
            throws MissingIpAddressException, LocationException, UserException {
        if (next == null)
            return;
        next.validate(data);
    }
}
