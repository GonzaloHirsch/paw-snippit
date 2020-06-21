package ar.edu.itba.paw.models;

import javax.persistence.*;
import java.time.Instant;
import java.util.Collection;
import java.util.HashSet;

@Entity
@Table(name = "snippets")
public class Snippet {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "snippets_id_seq")
    @SequenceGenerator(allocationSize = 1, sequenceName = "snippets_id_seq", name="snippets_id_seq")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User owner;

    @Column(length = 6000, name = "code", nullable = false)
    private String code;

    @Column(length = 50, name = "title", nullable = false)
    private String title;

    @Column(length = 500, name = "description")
    private String description;

    @Column(name = "date_created")
    private Instant dateCreated;

    @Column(name = "flagged")
    private boolean flagged;

    @Column(name = "deleted")
    private boolean deleted;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true, fetch = FetchType.LAZY, mappedBy = "snippet")
    private Collection<Vote> votes = new HashSet<>();

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true, fetch = FetchType.LAZY, mappedBy = "snippet")
    private Collection<Report> reports = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "favorites", cascade = CascadeType.PERSIST)
    private Collection<User> userFavorites = new HashSet<>();


    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "snippet_tags",
            joinColumns = @JoinColumn(name = "snippet_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private Collection<Tag> tags = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "language_id", referencedColumnName = "id")
    private Language language;

    protected Snippet(){
        // Hibernate constructor
    }

    public Snippet(User owner, String code, String title, String description, Instant dateCreated, Language language, Collection<Tag> tags, boolean flagged, boolean deleted) {
        this.owner = owner;
        this.code = code;
        this.title = title;
        this.description = description;
        this.dateCreated = dateCreated;
        this.language = language;
        this.tags = tags;
        this.flagged = flagged;
        this.deleted = deleted;
    }

    /* Constructors for service tests */
    public Snippet(Long id, User owner, String code, String title, String description, Instant dateCreated, Language language, Collection<Tag> tags, boolean flagged, boolean deleted) {
        this.id = id;
        this.owner = owner;
        this.code = code;
        this.title = title;
        this.description = description;
        this.dateCreated = dateCreated;
        this.language = language;
        this.tags = tags;
        this.flagged = flagged;
        this.deleted = deleted;
    }

    /**
     * Returns the string representation of the creation date
     * @return
     */
    public Instant getCreationDate(){
        return this.dateCreated;
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
        return this.votes.stream().mapToInt(Vote::getVoteWeight).sum();
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

    public Instant getDateCreated() {
        return this.dateCreated;
    }

    public void setDateCreated(Instant dateCreated) {
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

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public Collection<Report> getReports() {
        return reports;
    }

    public void setReports(Collection<Report> reports) {
        this.reports = reports;
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

    @Override public int hashCode() {
        return Long.hashCode(this.getId());
    }

    /**
     * Returns a brief description of this snippet. The exact details
     * of the representation are unspecified and subject to change,
     * but the following may be regarded as typical:
     *
     * "[Snippet #12: title=Floyd Algorithm, owner=user123]"
     */
    @Override public String toString() {
        return String.format("Snippet #%d: title=%s, owner=%s]", this.getId(), this.getTitle(), this.getOwner());
    }

}
