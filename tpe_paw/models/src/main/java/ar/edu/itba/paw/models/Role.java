package ar.edu.itba.paw.models;

import javax.persistence.*;

@Entity
@Table(name="roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_id_seq")
    @SequenceGenerator(allocationSize = 1, sequenceName = "users_id_seq", name="users_id_seq")
    private Long id;

    @Column(length = 20)
    private String name;

    //For hiberante
    public Role(){}

    @Deprecated
    public Role(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public long getId() { return id; }

    public String getName() { return name; }
}
