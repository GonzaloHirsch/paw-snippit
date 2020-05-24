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

@Repository
public class RoleJpaDaoImpl implements RoleDao {

    @PersistenceContext
    private EntityManager em;

    private final static String USER_ROLE = "USER";
    private final static String ADMIN_ROLE = "ADMIN";

    private void addToUserRoles(final long userId, final long roleId) {
        Optional<User> maybeUser = Optional.ofNullable(this.em.find(User.class, userId));
        Optional<Role> maybeRole = Optional.ofNullable(this.em.find(Role.class,roleId));
        if(maybeUser.isPresent() && maybeRole.isPresent()) {
            User user = maybeUser.get();
            user.getRoles().add(maybeRole.get());
            this.em.persist(user);
        }
    }

    @Override
    public Collection<Role> getAllRoles() {
        return this.em.createQuery("from Role", Role.class).getResultList();
    }

    //TODO: See if its better to move to UserJpaDaoImpl
    @Override
    public Collection<String> getUserRoles(long userId) {
        Query nativeQuery = this.em.createNativeQuery("SELECT ur.role_id FROM user_roles AS ur WHERE ur.user_id = :id");
        nativeQuery.setParameter("id", userId);
        List<Long> roleIds = ((List<Integer>) nativeQuery.getResultList())
                .stream().map(i -> i.longValue()).collect(Collectors.toList());
        final TypedQuery<Role> query = this.em.createQuery("from Role where id IN :roleIds", Role.class);
        query.setParameter("roleIds", roleIds);
        return query.getResultList().stream().map(Role::getName).collect(Collectors.toList());
    }

    @Override
    public void assignUserRole(long userId) {
        final TypedQuery<Role> query = this.em.createQuery("from Role as r where r.name = :name", Role.class)
                .setParameter("name", USER_ROLE);
        Optional<Role> userRole = query.getResultList().stream().findFirst();
        userRole.ifPresent(role -> this.addToUserRoles(userId, role.getId()));
    }

    @Override
    public void assignAdminRole(long userId) {
        final TypedQuery<Role> query = this.em.createQuery("from Role as r where r.name = :name", Role.class)
                .setParameter("name", ADMIN_ROLE);
        Optional<Role> adminRole = query.getResultList().stream().findFirst();
        adminRole.ifPresent(role -> this.addToUserRoles(userId, role.getId()));
    }

    @Override
    public String getAdminRoleName() { return ADMIN_ROLE; }

    @Override
    public String getUserRoleName() { return USER_ROLE; }
}
