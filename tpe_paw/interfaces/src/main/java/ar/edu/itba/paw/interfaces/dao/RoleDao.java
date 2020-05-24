package ar.edu.itba.paw.interfaces.dao;


import ar.edu.itba.paw.models.Role;
import ar.edu.itba.paw.models.User;

import java.util.Collection;

public interface RoleDao {
    Collection<Role> getAllRoles();
    Collection<String> getUserRoles(final User user);
    void assignUserRole(final long userId);
    void assignAdminRole(final long userId);

    String getAdminRoleName();
    String getUserRoleName();
}
