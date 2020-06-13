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

    protected Language(){
        // Hibernate constructor
    }

    public Language(String name) {
        this.name = name;
        this.deleted = false;
    }

    public Long getId() { return id; }

    public String getName() { return name; }

    public Collection<Snippet> getSnippetsUsing() {
        return snippetsUsing;
    }

    public void setSnippetsUsing(Collection<Snippet> snippetsUsing) {
        this.snippetsUsing = snippetsUsing;
    }
    public boolean snippetsUsingIsEmpty() {
        return this.getSnippetsUsing().stream().allMatch(Snippet::isDeleted);
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
}
