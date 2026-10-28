package org.example;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.Collections;

public class Database {
    // instanta unica -> Singleton
    private static Database instance;

    private final Set<Server> servers = new LinkedHashSet<>();
    private final Set<ResourceGroup> groups = new LinkedHashSet<>();
    private final Set<User> users = new LinkedHashSet<>();
    private final Set<Alert> alerts = new LinkedHashSet<>();

    private Database() {};

    public static synchronized Database getInstance() {
        if (instance == null)
            instance = new Database();
        return instance;
    }

    public void addUser(User user) {
        if (user != null)
            users.add(user);
    }
    public void addServer(Server server) {
        if (server != null)
            servers.add(server);
    }
    public void addResourceGroup(ResourceGroup group) {
        if (group != null)
            groups.add(group);
    }
    public void addAlert(Alert alert) {
        if (alert != null)
            alerts.add(alert);
    }

    public boolean removeResourceGroup(String ipAddress) {
        return groups.removeIf(group -> group.getIpAddress().equals(ipAddress));
    }

    // gettere
    public Server getServer(String ip) {
        for (Server server: servers)
            if (server.getIpAddress().equals(ip))
                return server;
        return null;
    }
    public ResourceGroup getResourceGroup(String ip) {
        for (ResourceGroup group: groups)
            if (group.getIpAddress().equals(ip))
                return group;
        return null;
    }
    public User getUser(String name) {
        for (User user: users)
            if (user.getName().equals(name))
                return user;
        return null;
    }

    public Set<Server> getAllServers() { return Collections.unmodifiableSet(servers); }
    public Set<User> getAllUsers() { return Collections.unmodifiableSet(users); }
    public Set<ResourceGroup> getAllGroups() { return Collections.unmodifiableSet(groups); }
    public Set<Alert> getAllAlerts() { return Collections.unmodifiableSet(alerts); }

    public void clear() {
        servers.clear();
        groups.clear();
        users.clear();
        alerts.clear();
    }
}
