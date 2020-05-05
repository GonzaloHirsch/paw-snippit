package ar.edu.itba.paw.interfaces.service;

import ar.edu.itba.paw.models.Role;
import ar.edu.itba.paw.models.User;

import java.util.Collection;

public interface RoleService {
    String getAdminRole();
    String getUserRole();

    void assignAdminRole(long userId);

    void assignUserRole(long userId);

    Collection<String> getUserRoles(long userId);

    boolean isAdmin(long userId);
}
