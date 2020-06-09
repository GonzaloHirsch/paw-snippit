package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Role;
import ar.edu.itba.paw.models.Snippet;
import ar.edu.itba.paw.models.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.*;
import java.util.stream.Collectors;

import ar.edu.itba.paw.interfaces.dao.RoleDao;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class RoleJpaDaoImpl implements RoleDao {

    @PersistenceContext
    private EntityManager em;

    private final static String USER_ROLE = "USER";
    private final static String ADMIN_ROLE = "ADMIN";

    private boolean addToUserRoles(final long userId, final long roleId) {
        Optional<User> user = Optional.ofNullable(this.em.find(User.class, userId));
        Optional<Role> role = Optional.ofNullable(this.em.find(Role.class,roleId));
        if(user.isPresent() && role.isPresent()) {
            user.get().addRole(role.get());
            this.em.persist(user.get());
        }
        return user.isPresent();
    }

    @Override
    public Collection<Role> getAllRoles() {
        return this.em.createQuery("from Role", Role.class).getResultList();
    }

    //TODO: See if its better to move to UserJpaDaoImpl
    @Override
    public Collection<String> getUserRoles(long userId) {
        Optional<User> user = em.createQuery("SELECT u from User u JOIN FETCH u.roles WHERE u.id = :userId", User.class)
                .setParameter("userId", userId)
                .getResultList().stream().findFirst();

        return user.map(value -> value.getRoles().stream().map(Role::getName).collect(Collectors.toList())).orElse(Collections.emptyList());
    }

    @Override
    @Transactional
    public boolean assignUserRole(long userId) {
        final TypedQuery<Role> query = this.em.createQuery("from Role as r where r.name = :name", Role.class)
                .setParameter("name", USER_ROLE);
        Optional<Role> userRole = query.getResultList().stream().findFirst();
        return userRole.filter(role -> this.addToUserRoles(userId, role.getId())).isPresent();
    }

    @Override
    @Transactional
    public boolean assignAdminRole(long userId) {
        final TypedQuery<Role> query = this.em.createQuery("from Role as r where r.name = :name", Role.class)
                .setParameter("name", ADMIN_ROLE);
        Optional<Role> adminRole = query.getResultList().stream().findFirst();
        return adminRole.filter(role -> this.addToUserRoles(userId, role.getId())).isPresent();
    }

    @Override
    public String getAdminRoleName() { return ADMIN_ROLE; }

    @Override
    public String getUserRoleName() { return USER_ROLE; }
}
