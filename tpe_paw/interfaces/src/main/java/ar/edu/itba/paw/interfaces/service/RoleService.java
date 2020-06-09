package ar.edu.itba.paw.interfaces.service;

import java.util.Collection;

public interface RoleService {
    String getAdminRoleName();
    String getUserRoleName();

    boolean assignAdminRole(long userId);

    boolean assignUserRole(long userId);

    Collection<String> getUserRoles(long userId);

    boolean isAdmin(long userId);
}
