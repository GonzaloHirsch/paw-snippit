package ar.edu.itba.paw.models;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;
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

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "roles", cascade = CascadeType.PERSIST)
    private Collection<User> usersWithRole = new HashSet<>();

    public Role(){
        // Hibernate constructor
    }

    public Role(String name) {
        this.name = name;
    }

    public Long getId() { return this.id; }

    public Collection<User> getUsersWithRole() {
        return this.usersWithRole;
    }

    public void setUsersWithRole(Collection<User> usersWithRole) {
        this.usersWithRole = usersWithRole;
    }

    public String getName() { return this.name;  }

    @Override
    public boolean equals(Object o) {

        if (o == this) {
            return true;
        }
        if (!(o instanceof Role)) {
            return false;
        }
        Role role = (Role) o;
        return this.getId().equals(role.getId()) && this.getName().equals(role.getName());
    }

    @Override
    public int hashCode() {
        int result = this.getId().hashCode();
        result = 31 * result + this.getName().hashCode();
        return result;
    }

    /**
     * Returns a brief description of this role. The exact details
     * of the representation are unspecified and subject to change,
     * but the following may be regarded as typical:
     *
     * "[Role #1: name=USER]"
     */
    @Override public String toString() {
        return String.format("Role #%d: name=%s]", this.getId(), this.getName());
    }
}
