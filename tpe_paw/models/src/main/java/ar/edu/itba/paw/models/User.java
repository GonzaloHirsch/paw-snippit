package ar.edu.itba.paw.models;

import java.util.Locale;

public class User {
    private long id;
    private String username;
    private String password;
    private String email;
    private String description;
    private int reputation;
    private String dateJoined;
    private byte[] icon;
    private Locale locale;
    private boolean verified;

    public User(long id, String username, String password, String email, String dateJoined, byte[] icon, Locale locale, boolean verified) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.description = "";
        this.reputation = 0;
        this.dateJoined = dateJoined;
        this.icon = icon;
        this.locale = locale;
        this.verified = verified;
    }

    public User(long id, String username, String password, String email, String description, int reputation, String dateJoined, byte[] icon, Locale locale, boolean verified) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.description = description;
        this.reputation = reputation;
        this.dateJoined = dateJoined;
        this.icon = icon;
        this.locale = locale;
        this.verified = verified;
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

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }
}
