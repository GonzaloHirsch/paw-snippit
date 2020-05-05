package ar.edu.itba.paw.interfaces.service;

import ar.edu.itba.paw.models.Role;
import ar.edu.itba.paw.models.User;

import java.util.Collection;

public interface RoleService {
    Role getAdminRole();
    Role getUserRole();

    void assignUserRole(User user);

    void assignAdminRole(User user);

    Collection<String> getUserRoles(long userId);
}
