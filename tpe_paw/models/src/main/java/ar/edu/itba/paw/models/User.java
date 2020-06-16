package ar.edu.itba.paw.models;

import javax.persistence.*;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

@Entity
@Table(name = "users")
public class User {

    public static final DateTimeFormatter DATE = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss").withLocale(Locale.UK).withZone(ZoneId.systemDefault());

    //TODO: Check correct sequence generator
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_id_seq")
    @SequenceGenerator(allocationSize = 1, sequenceName = "users_id_seq", name="users_id_seq")
    private Long id;

    @Column(length = 30, name="username")
    private String username;

    @Column(length=60, name="password")
    private String password;

    @Column(length = 60, name="email")
    private String email;

    @Column(length = 300, name="description")
    private String description;

    @Column(name="reputation")
    private int reputation;

    @Column(name = "date_joined")
    private Instant dateJoined;

    @Column(name="icon")
    private byte[] icon;

    @Column(name="verified")
    private boolean verified;

    @Column(length = 5, name="lang")
    private String lang = "en";     // Setting "en" as the default language value

    @Column(length = 5, name="region")
    private String region = "US";   // Setting "US" as the default location value

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinTable(
        name= "user_roles",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Collection<Role> roles = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private Collection<Vote> votes = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinTable(
            name= "favorites",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "snippet_id"))
    private Collection<Snippet> favorites= new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "follows",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private Collection<Tag> followedTags = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "owner", cascade = CascadeType.PERSIST)
    private Collection<Snippet> createdSnippets = new HashSet<>();

    protected User() {
        // Hibernate constructor
    }

    public User(String username, String password, String email, Instant dateJoined, Locale locale, boolean verified) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.description = "";
        this.reputation = 0;
        this.dateJoined = dateJoined;
        this.lang = locale.getLanguage();
        this.region = locale.getCountry();
        this.verified = verified;
    }

    public Long getId() { return id; }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getReputation() {
        return reputation;
    }

    public void setReputation(int reputation) {
        this.reputation = reputation;
    }

    /**
     * Returns the string representation of the creation date
     * @return
     */
    public String getDateJoined(){
        return DATE.format(this.dateJoined);
    }

    public Locale getLocale(){
        return new Locale(this.lang, this.region);
    }

    public byte[] getIcon() { return this.icon; }

    public void setIcon(byte[] icon) {
        this.icon = icon;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getRegion() {
        return region;
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

    public Collection<Snippet> getCreatedSnippets() {
        return this.createdSnippets;
    }

    public Collection<Tag> getFollowedTags() {
        return this.followedTags;
    }

    public void setFollowedTags(Collection<Tag> followedTags) {
        this.followedTags = followedTags;
    }

    public Collection<Vote> getVotes() {
        return votes;
    }

    public void setVotes(Collection<Vote> votes) {
        this.votes = votes;
    }

    public Collection<Role> getRoles() {
        return roles;
    }

    public void setRoles(Collection<Role> roles) {
        this.roles = roles;
    }

    public Collection<Snippet> getFavorites() {
        return this.favorites;
    }

    public void setFavorites(Collection<Snippet> favorites) {
        this.favorites = favorites;
    }

    public void addFavorite(Snippet snippet) {
        this.getFavorites().add(snippet);
        snippet.getUserFavorites().add(this);
    }

    public void removeFavorite(Snippet snippet) {
        this.getFavorites().remove(snippet);
        snippet.getUserFavorites().remove(this);
    }

    public void addRole(Role role) {
        this.getRoles().add(role);
        role.getUsersWithRole().add(this);
    }

    public void followTag(Tag tag) {
        this.getFollowedTags().add(tag);
        tag.getFollowingUsers().add(this);
    }

    public void unfollow(Tag tag) {
        this.getFollowedTags().remove(tag);
        tag.getFollowingUsers().remove(this);
    }

    @Override
    public boolean equals(Object o) {

        if (o == this) {
            return true;
        }
        if (!(o instanceof User)) {
            return false;
        }
        User user = (User) o;
        return this.getId().equals(user.getId());
    }

    @Override public int hashCode() {
        return this.getId().hashCode();
    }

    /**
     * Returns a brief description of this user. The exact details
     * of the representation are unspecified and subject to change,
     * but the following may be regarded as typical:
     *
     * "[User #12: username=user123, email=user123@gmail.com]"
     */
    @Override public String toString() {
        return String.format("User #%d: username=%s, email=%s]", this.getId(), this.getUsername(), this.getEmail());
    }
}
