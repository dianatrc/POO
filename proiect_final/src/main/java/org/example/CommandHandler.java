package org.example;

import java.util.List;

public class CommandHandler {
    private final String pathType;
    private final BaseValidator ipValidator;

    public CommandHandler(String pathType, BaseValidator ipValidator) {
        this.pathType = pathType;
        this.ipValidator = ipValidator;
    }

    public void addServer(List<String> data, int indexLine) {
        try {
            // validate (chain of responsability)
            ipValidator.validate(data);

            String hostname = data.get(1);
            String ip = data.get(2);
            ServerStatus status = ServerStatus.valueOf(data.get(3).toUpperCase());
            String country = data.get(4);

            Location.Builder locationBuilder = new Location.Builder(country);
            if (!data.get(5).isEmpty())
                locationBuilder.withCity(data.get(5));
            if (!data.get(6).isEmpty())
                locationBuilder.withAddress(data.get(6));
            if (!data.get(7).isEmpty())
                locationBuilder.withLatitude(Double.parseDouble(data.get(7)));
            if (!data.get(8).isEmpty())
                locationBuilder.withLongitude(Double.parseDouble(data.get(8)));
            Location location = locationBuilder.build();

            User user = UserFactory.createUser(
                    data.get(9), data.get(10), data.get(11),
                    data.get(12), data.get(13)
            );
            Database.getInstance().addUser(user);

            Server.Builder serverBuilder = new Server.Builder(ip, location, user)
                    .withHostName(hostname)
                    .withStatus(status);
            if (!data.get(14).isEmpty())
                serverBuilder.withCpuCores(Integer.parseInt(data.get(14)));
            if (!data.get(15).isEmpty())
                serverBuilder.withRamGb(Integer.parseInt(data.get(15)));
            if (!data.get(16).isEmpty())
                serverBuilder.withStorageGb(Integer.parseInt(data.get(16)));
            Server server = serverBuilder.build();
            Database.getInstance().addServer(server);

            System.out.println("ADD SERVER: " + ip + ": " + data.get(3).toUpperCase());

        } catch (MissingIpAddressException | UserException | LocationException e) {
            System.out.println("ADD SERVER: " + e.toString() + " ## line no: " + indexLine);
        }
    }

    public void addGroup(List<String> data, int indexLine) {
        if (!isCorrectPath()) return;

        try {
            ipValidator.validate(data);

            String ip = data.get(1);
            Server server = Database.getInstance().getServer(ip);

            ResourceGroup group = new ResourceGroup(ip);
            Database.getInstance().addResourceGroup(group);

            if (server != null) {
                server.addObserver(group);  // inregistrare Observer Pattern
            }
            System.out.println("ADD GROUP: " + ip);
        } catch (MissingIpAddressException | UserException | LocationException e) {
            System.out.println("ADD GROUP: " + e.toString() + " ## line no: " + indexLine);
        }
    }

    public void findGroup(List<String> data, int indexLine) {
        if (!isCorrectPath()) return;
        try {
            ipValidator.validate(data);

            String ip = data.get(1);
            ResourceGroup group = Database.getInstance().getResourceGroup(ip);

            if (group != null) {
                System.out.println("FIND GROUP: " + ip);
            } else {
                System.out.println("FIND GROUP: Group not found: ipAddress = " + ip);
            }
        } catch (MissingIpAddressException | UserException | LocationException e) {
            System.out.println("FIND GROUP: " + e.toString() + " ## line no: " + indexLine);
        }
    }

    public void removeGroup(List<String> data, int indexLine) {
        if (!isCorrectPath()) return;
        try {
            ipValidator.validate(data);

            String ip = data.get(1);

            if (Database.getInstance().removeResourceGroup(ip)) {
                System.out.println("REMOVE GROUP: " + ip);
            } else {
                System.out.println("REMOVE GROUP: Group not found: ipAddress = " + ip);
            }
        } catch (MissingIpAddressException | UserException | LocationException e) {
            System.out.println("REMOVE GROUP: " + e.toString() + " ## line no: " + indexLine);
        }
    }

    public void addMember(List<String> data, int indexLine) {
        if (!isCorrectPath()) return;
        try {
            ipValidator.validate(data);

            String ip = data.get(1);
            String name = data.get(2);
            String role = data.get(3);
            String email = data.get(4);
            String department = data.get(5);
            String clearance = data.get(6);

            User member = UserFactory.createUser(name, role, email, department, clearance);

            Database.getInstance().addUser(member);
            ResourceGroup group = Database.getInstance().getResourceGroup(ip);

            if (group != null) {
                group.addMember(member);
                System.out.println("ADD MEMBER: " + ip + ": name = " + name + " && role = " + role);
            }
        } catch (MissingIpAddressException | UserException | LocationException e) {
            System.out.println("ADD MEMBER: " + e.toString() + " ## line no: " + indexLine);
        }
    }

    public void findMember(List<String> data, int indexLine) {
        if (!isCorrectPath()) return;

        try {
            ipValidator.validate(data);

            String ip = data.get(1);
            String name = data.get(2);
            String role = data.get(3);

            ResourceGroup group = Database.getInstance().getResourceGroup(ip);
            if (group != null){
                User member = group.findMember(name, role);
                if (member != null)
                    System.out.println("FIND MEMBER: " + ip +
                            ": name = " + name + " && role = " + role);
                else
                    System.out.println("FIND MEMBER: Member not found: ipAddress = " +
                            ip + ": name = " + name + " && role = " + role);
            } else {
                System.out.println("FIND MEMBER: Group not found: ipAddress = " + ip);
            }
        } catch (MissingIpAddressException | UserException | LocationException e) {
            System.out.println("FIND MEMBER: " + e.toString() + " ## line no: " + indexLine);
        }
    }

    public void removeMember(List<String> data, int indexLine) {
        if (!isCorrectPath()) return;
        try {
            ipValidator.validate(data);

            String ip = data.get(1);
            String name = data.get(2);
            String role = data.get(3);

            ResourceGroup group = Database.getInstance().getResourceGroup(ip);
            if (group != null) {
                User member = group.findMember(name, role);
                if (member != null) {
                    group.removeMember(member);
                    System.out.println("REMOVE MEMBER: " + ip + ": name = " +
                            name + " && role = " + role);
                } else {
                    System.out.println("REMOVE MEMBER: Member not found: ipAddress = " +
                            ip + ": name = " + name + " && role = " + role);
                }
            } else {
                System.out.println("REMOVE MEMBER: Group not found: ipAddress = " + ip);
            }
        } catch (MissingIpAddressException | UserException | LocationException e) {
            System.out.println("REMOVE MEMBER: " + e.toString() + " ## line no: " + indexLine);
        }
    }

    public void addEvent(List<String> data) {
        if (!isCorrectPath()) return;

        AlertType type = AlertType.valueOf(data.get(1).toUpperCase());
        Severity severity = Severity.valueOf(data.get(2).toUpperCase());
        String ip = data.get(3);
        String message = data.get(4);

        Server server = Database.getInstance().getServer(ip);
        if (server != null) {
            server.addAlert(type, severity, message);
        }
    }

    private boolean isCorrectPath() {
        return pathType.equalsIgnoreCase(PathTypes.GROUPS.getValue()) ||
                pathType.equalsIgnoreCase(PathTypes.LISTENER.getValue());
    }
}
