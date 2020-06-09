package ar.edu.itba.paw.interfaces.dao;


import ar.edu.itba.paw.models.Role;
import ar.edu.itba.paw.models.User;

import java.util.Collection;

public interface RoleDao {
    Collection<Role> getAllRoles();
    Collection<String> getUserRoles(final long userId);
    boolean assignUserRole(final long userId);
    boolean assignAdminRole(final long userId);

    String getAdminRoleName();
    String getUserRoleName();
}
