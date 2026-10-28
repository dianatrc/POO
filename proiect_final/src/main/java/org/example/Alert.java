package org.example;

import javax.management.MBeanServerInvocationHandler;

public class Alert {
    private final AlertType type;
    private final Severity severity;
    private final String message;
    private final String ipAddress;

    public Alert(AlertType type, Severity severity, String message, String ip) {
        this.type = type;
        this.severity = severity;
        this.message = message;
        this.ipAddress = ip;
    }

    public AlertType getType() { return type; }
    public Severity getSeverity() { return severity; }
    public String getMessage() { return message; }
    public String getIpAddress() { return ipAddress; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Alert)) return false;

        Alert other = (Alert) o;
        if (this.type != other.type) return false;
        if (this.severity != other.severity) return false;
        if (!this.message.equals(other.message)) return false;
        if (!this.ipAddress.equals(other.ipAddress)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(type, severity, message, ipAddress);
    }
}
