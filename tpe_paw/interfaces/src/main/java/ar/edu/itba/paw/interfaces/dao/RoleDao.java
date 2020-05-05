package ar.edu.itba.paw.interfaces.dao;


import ar.edu.itba.paw.models.Role;

import java.util.Collection;

public interface RoleDao {
    Collection<Role> getAllRoles();
    Collection<String> getUserRoles(final long userId);
    String assignUserRole(final long userId);
    String assignAdminRole(final long userId);

    Role getAdminRole();
    Role getUserRole();
}
