package org.example;

import java.util.ArrayList;
import java.util.List;

public class ResourceGroup implements Observer {
    private List<User> members = new ArrayList<>();
    private final String ipAddress;

    public ResourceGroup(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public void addMember(User member) {
        if (member != null && !members.contains(member))
            members.add(member);
    }

    public List<User> getMembers() { return  members; }
    public String getIpAddress() { return ipAddress; }

    public void removeMember(User member) {
        if (!members.isEmpty())
            members.remove(member);
    }

    public User findMember(String name, String role) {
        for (User u: getMembers())
            if (u.getName().equals(name) && u.getRole().equals(role))
                return u;
        return null;
    }

    @Override
    public void update(String ip, AlertType type, Severity severity, String message) {
        System.out.println(String.format("ADD EVENT: %s: type = %s && severity = %s && message = %s", ip,
                type.getValue(), severity.getValue(), message));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ResourceGroup)) return false;
        ResourceGroup that = (ResourceGroup) o;
        return ipAddress.equals(that.ipAddress);
    }

    @Override
    public int hashCode() {
        return ipAddress.hashCode();
    }

}
