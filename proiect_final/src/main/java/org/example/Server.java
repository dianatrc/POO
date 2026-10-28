package org.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Server {
    private final String ipAddress;
    private final Location location;
    private final User owner;

    private final String hostname;
    private final ServerStatus status;
    private final Integer cpuCores;
    private final Integer ramGb;
    private final Integer storageGb;

    // constructor privat apelat doar de Builder
    private Server(Server.Builder builder) {
        this.ipAddress = builder.ipAddress;
        this.location = builder.location;
        this.owner = builder.owner;
        this.hostname = builder.hostname;
        this.status = builder.status;
        this.cpuCores = builder.cpuCores;
        this.ramGb = builder.ramGb;
        this.storageGb = builder.storageGb;
    }

    public static class Builder {
        // campuri obligatorii
        private final String ipAddress;
        private final Location location;
        private final User owner;

        // campuri optionale
        private String hostname;
        private ServerStatus status;
        private Integer cpuCores;
        private Integer ramGb;
        private Integer storageGb;

        public Builder(String ipAddress, Location location, User owner) {
            this.ipAddress = ipAddress;
            this.location = location;
            this.owner = owner;
        }

        public Builder withHostName(String hostname) {
            this.hostname = hostname;
            return this;
        }

        public Builder withStatus(ServerStatus status) {
            this.status = status;
            return this;
        }

        public Builder withCpuCores(Integer cpuCores) {
            this.cpuCores = cpuCores;
            return this;
        }

        public Builder withRamGb(Integer ramGb) {
            this.ramGb = ramGb;
            return this;
        }

        public Builder withStorageGb(Integer storageGb) {
            this.storageGb = storageGb;
            return this;
        }

        public Server build() { return new Server(this); }
    }

    // gettere
    public String getIpAddress() { return ipAddress; }
    public Location getLocation() { return location; }
    public User getOwner() { return owner; }
    public String getHostname() { return hostname; }
    public ServerStatus getStatus() { return status; }
    public Integer getCpuCores() { return cpuCores; }
    public Integer getRamGb() { return ramGb; }
    public Integer getStorageGb() { return storageGb; }

    private final List<Observer> observers = Collections.synchronizedList(new ArrayList<>());

    public void addObserver(Observer observer) {
        if (observer == null || observers.contains(observer))
            return;
        observers.add(observer);
    }

    public void removeObserver(Observer observer) {
        if (observer == null)
            return;
        observers.remove(observer);
    }

    public void notifyObservers(AlertType type, Severity severity, String message) {
        for (Observer observer: observers)
            observer.update(ipAddress, type, severity, message);
    }

    public void addAlert(AlertType type, Severity severity, String message) {
        Alert alert = new Alert(type, severity, message, this.ipAddress);
        Database.getInstance().addAlert(alert);
        notifyObservers(type, severity, message);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Server))
            return false;
        return ipAddress.equals(((Server)o).ipAddress);
    }

    @Override
    public int hashCode() {
        return ipAddress.hashCode();
    }
}
