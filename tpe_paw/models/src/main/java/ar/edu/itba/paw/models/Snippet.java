package ar.edu.itba.paw.models;

import javax.persistence.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

@Entity
@Table(name = "snippets")
public class Snippet {

    public static final DateTimeFormatter DATE = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss").withLocale(Locale.UK).withZone(ZoneId.systemDefault());

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "snippets_id_seq")
    @SequenceGenerator(allocationSize = 1, sequenceName = "snippets_id_seq", name="snippets_id_seq")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User owner;

    @Column(length = 6000, name = "code", nullable = false)
    private String code;

    @Column(length = 50, name = "title", nullable = false)
    private String title;

    @Column(length = 500, name = "description")
    private String description;

    @Column(name = "date_created")
    private Timestamp dateCreated;

    @Column(name = "flagged")
    private boolean flagged;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY, mappedBy = "snippet")
    private Collection<Vote> votes;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "favorites", cascade = CascadeType.PERSIST)
    private Collection<User> userFavorites;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "snippet_tags",
            joinColumns = @JoinColumn(name = "snippet_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private Collection<Tag> tags;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "language_id", referencedColumnName = "id")
    private Language language;

    protected Snippet(){
        // Hibernate constructor
    }

    public Snippet(User owner, String code, String title, String description, Timestamp dateCreated, Language language, Collection<Tag> tags, boolean flagged) {
        this.owner = owner;
        this.code = code;
        this.title = title;
        this.description = description;
        this.dateCreated = dateCreated;
        this.language = language;
        this.tags = tags;
        this.flagged = flagged;
    }

    @Deprecated
    public Snippet(long id, User owner, String code, String title, String description, String dateCreated, String language, Collection<Tag> tags, int votes, boolean flagged) {
        this.id = id;
        this.owner = owner;
        this.code = code;
        this.title = title;
        this.description = description;
//        this.dateCreated = dateCreated;
//        this.language = language;
        this.tags = tags;
//        this.votes = votes;
        this.flagged = flagged;
    }

    /**
     * Returns the string representation of the creation date
     * @return
     */
    public String getCreationDate(){
        return DATE.format(this.dateCreated.toInstant());
    }

    /**
     * Returns the name of the associated language
     * @return
     */
    public String getLanguageName(){
        return this.language.getName();
    }

    /**
     * Calculates the vote count for the snippet
     * @return
     */
    public int getVoteCount(){
        return this.votes.stream().mapToInt(Vote::getType).sum();
    }

    public boolean isFlagged() {
        return flagged;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Timestamp getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Timestamp dateCreated) {
        this.dateCreated = dateCreated;
    }

    public void setFlagged(boolean flagged) {
        this.flagged = flagged;
    }

    public Collection<Vote> getVotes() {
        return votes;
    }

    public void setVotes(Collection<Vote> votes) {
        this.votes = votes;
    }

    public Collection<User> getUserFavorites() {
        return userFavorites;
    }

    public void setUserFavorites(Collection<User> userFavorites) {
        this.userFavorites = userFavorites;
    }

    public Collection<Tag> getTags() {
        return tags;
    }

    public void setTags(Collection<Tag> tags) {
        this.tags = tags;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    @Override
    public boolean equals(Object o) {

        if (o == this) {
            return true;
        }
        if (!(o instanceof Snippet)) {
            return false;
        }
        Snippet snippet = (Snippet) o;
        return this.getId().equals(snippet.getId());
    }
}
