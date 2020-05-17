package ar.edu.itba.paw.models;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

@Entity
@Table(name = "users")
public class User {

    //TODO: Check correct sequence generator
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_id_seq")
    @SequenceGenerator(allocationSize = 1, sequenceName = "users_id_seq", name="users_id_seq")
    private Long id;

    @Column(length = 30)
    private String username;

    @Column(length=60)
    private String password;

    @Column(length = 60)
    private String email;

    @Column(length = 300)
    private String description;

    @Column
    private int reputation;

    @Column
    private String dateJoined;

    @Column
    private byte[] icon;

    @Column
    private boolean verified;

    @Column(length = 5)
    private String lang = "en";     // Setting "en" as the default language value

    @Column(length = 5)
    private String region = "US";   // Setting "US" as the default location value

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name= "user_roles",
        joinColumns = @JoinColumn(name = "user_id"),        //TODO: CHECK IF IS users OR user
        inverseJoinColumns = @JoinColumn(name = "role_id")) //TODO: SAME with role
    private Collection<Role> roles;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private Collection<Vote> votes;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name= "favorites",
            joinColumns = @JoinColumn(name = "user_id"),             //TODO: CHECK IF IS users OR user
            inverseJoinColumns = @JoinColumn(name = "snippet_id"))   //TODO: SAME with snippet
    private Collection<Snippet> favorites;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "follows",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private Collection<Tag> followedTags;

    @OneToMany(fetch = FetchType.LAZY)
    private Collection<Snippet> createdSnippets;

    //TODO: Delete
    @Deprecated
    private Locale locale;

    protected User() {
        // Hibernate constructor
    }

    @Deprecated
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

    @Deprecated
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

    public Long getId() { return id; }

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

    public String getLang() {
        return lang;
    }

    public String getRegion() {
        return region;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }
}
