package ar.edu.itba.paw.models;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

public class User {
    private long id;
    private String username;
    private String password;
    private String email;
    private String description;
    private int reputation;
    private String dateJoined;
    private byte[] icon;
    private Collection<String> roles;

    public User(long id, String username, String password, String email, String dateJoined, byte[] icon) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.description = "";
        this.reputation = 0;
        this.dateJoined = dateJoined;
        this.icon = icon;
        this.roles = new ArrayList<>();
    }

    public User(long id, String username, String password, String email, String description, int reputation, String dateJoined, byte[] icon) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.description = description;
        this.reputation = reputation;
        this.dateJoined = dateJoined;
        this.icon = icon;
        this.roles = new ArrayList<>();
    }

    public long getId() { return id; }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getDescription() {
        return description;
    }

    public int getReputation() {
        return reputation;
    }

    public String getDateJoined() {
        return dateJoined;
    }

    public byte[] getIcon() { return this.icon; }

    public Collection<String> getRoles() {
        return roles;
    }
    public void setRoles(Collection<String> roles) {
        this.roles = roles;
    }

    public void addRole(String role) {
        this.roles.add(role);
    }
}
