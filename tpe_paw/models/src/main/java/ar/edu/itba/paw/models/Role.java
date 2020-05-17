package ar.edu.itba.paw.models;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="roles")
public class Role {

    //TODO: Check correct sequence generator
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "roles_id_seq")
    @SequenceGenerator(allocationSize = 1, sequenceName = "roles_id_seq", name="roles_id_seq")
    private Long id;

    @Column(length = 20)
    private String name;

    @ManyToMany(mappedBy = "roles")
    List<User> users;

    //For hibernate
    public Role(){}

    @Deprecated
    public Role(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public long getId() { return id; }

    public String getName() { return name; }
}
