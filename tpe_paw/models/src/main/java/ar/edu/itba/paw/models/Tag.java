package ar.edu.itba.paw.models;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "tags")
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tags_id_seq")
    @SequenceGenerator(allocationSize = 1, sequenceName = "tags_id_seq", name="tags_id_seq")
    private Long id;

    @Column(name = "name", length = 30, unique = true)
    private String name;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "tags", cascade = CascadeType.PERSIST)
    private Collection<Snippet> snippetsUsing;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "followedTags", cascade = CascadeType.PERSIST)
    private Collection<User> followingUsers;

    public Tag(String name) {
        this.name = name;
    }

    @Deprecated
    public Tag(long id, String name) {
        this.id = id;
        this.name = name;
    }

    protected Tag(){
        // Hibernate constructor
    }

    @Override
    public String toString() {
        return this.name;
    }

    public Long getId() { return this.id; }

    public String getName() { return this.name; }

    public Collection<Snippet> getSnippetsUsing(){ return this.snippetsUsing; }

    public Collection<User> getFollowingUsers() {
        return followingUsers;
    }

    public boolean snippetsUsingIsEmpty() {
        return this.getSnippetsUsing().stream().allMatch(Snippet::isDeleted);
    }

    @Override
    public boolean equals(Object o) {

        if (o == this) {
            return true;
        }
        if (!(o instanceof Tag)) {
            return false;
        }
        Tag tag = (Tag) o;
        return this.getId().equals(tag.getId());
    }
}
