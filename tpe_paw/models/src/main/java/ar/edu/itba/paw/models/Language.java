package ar.edu.itba.paw.models;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;

@Entity
@Table(name = "languages")
public class Language {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "languages_id_seq")
    @SequenceGenerator(allocationSize = 1, sequenceName = "languages_id_seq", name="languages_id_seq")
    private Long id;

    @Column(name = "name", length = 30, unique = true)
    private String name;

    @Column(name = "deleted")
    private boolean deleted;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "language", cascade = CascadeType.PERSIST)
    private Collection<Snippet> snippetsUsing = new HashSet<>();

    private Boolean snippetsUsingIsEmpty = null;

    protected Language(){
        // Hibernate constructor
    }

    public Language(String name) {
        this.name = name;
        this.deleted = false;
    }

    /* Constructor for service tests */
    public Language(Long id, String name, boolean deleted) {
        this.id = id;
        this.name = name;
        this.deleted = deleted;
    }

    public Long getId() { return id; }

    public String getName() { return name; }

    public Collection<Snippet> getSnippetsUsing() {
        return snippetsUsing;
    }

    public void setSnippetsUsing(Collection<Snippet> snippetsUsing) {
        this.snippetsUsing = snippetsUsing;
    }

    public Boolean getSnippetsUsingIsEmpty() {
        return snippetsUsingIsEmpty;
    }

    public void setSnippetsUsingIsEmpty(Boolean snippetsUsingIsEmpty) {
        this.snippetsUsingIsEmpty = snippetsUsingIsEmpty;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    public boolean equals(Object o) {

        if (o == this) {
            return true;
        }
        if (!(o instanceof Language)) {
            return false;
        }
        Language tag = (Language) o;
        return this.getId().equals(tag.getId());
    }

    @Override public int hashCode() {
        return this.getId().hashCode();
    }

    /**
     * Returns a brief description of this language. The exact details
     * of the representation are unspecified and subject to change,
     * but the following may be regarded as typical:
     *
     * "[Language #12: name=java]"
     */
    @Override public String toString() {
        return String.format("Language #%d: name=%s]", this.getId(), this.getName());
    }
}
