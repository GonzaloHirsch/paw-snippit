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

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "user_id", optional = false)
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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "snippet")
    private Collection<Vote> votes;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "favorites",
            joinColumns = @JoinColumn(name = "snippet_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Collection<User> userFavorites;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "snippet_tags",
            joinColumns = @JoinColumn(name = "snippet_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private Collection<Tag> tags;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "language_id", optional = false)
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

    public Long getId() {
        return id;
    }

    public User getOwner() {
        return owner;
    }

    public String getCode() {
        return code;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
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

    public Collection<Tag> getTags() {
        return tags;
    }

    public void setTags(Collection<Tag> tags) {
        this.tags = tags;
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

    public void setFlagged(boolean flagged) {
        this.flagged = flagged;
    }
}
