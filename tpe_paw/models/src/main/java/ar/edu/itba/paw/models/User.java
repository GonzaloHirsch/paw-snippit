package ar.edu.itba.paw.models;

import java.util.Date;

public class User {
    private long id;
    private String username;
    private String password;
    private String email;
    private String description;
    private int reputation;
    private Date dateJoined;

    public User(String username, String password, String email, String description, int reputation, Date dateJoined) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.description = description;
        this.reputation = reputation;
        this.dateJoined = dateJoined;
    }

    public long getUserId() { return id; }

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

    public Date getDateJoined() {
        return dateJoined;
    }
}
