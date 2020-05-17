package ar.edu.itba.paw.models;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "languages")
public class Language {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "languages_id_seq")
    @SequenceGenerator(allocationSize = 1, sequenceName = "languages_id_seq", name="languages_id_seq")
    private Long id;

    @Column(name = "name", length = 30, unique = true)
    private String name;

    @OneToMany(fetch = FetchType.LAZY)
    private Collection<Snippet> snippetsUsing;

    protected Language(){
        // Hibernate constructor
    }

    public Language(String name) {
        this.name = name;
    }

    @Deprecated
    public Language(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() { return id; }

    public String getName() { return name; }
}
