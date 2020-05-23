package ar.edu.itba.paw.models;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name="roles")
public class Role {

    //TODO: Check correct sequence generator
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "roles_id_seq")
    @SequenceGenerator(allocationSize = 1, sequenceName = "roles_id_seq", name="roles_id_seq")
    private Long id;

    @Column(length = 20, name = "role")
    private String name;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "roles")
    private Collection<User> usersWithRole;

    public Role(){
        // Hibernate constructor
    }

    @Deprecated
    public Role(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public long getId() { return id; }

    public Collection<User> getUsersWithRole() {
        return usersWithRole;
    }

    public String getName() { return name;



    }
}
