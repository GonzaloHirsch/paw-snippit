package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.dao.RoleDao;
import ar.edu.itba.paw.interfaces.service.RoleService;
import ar.edu.itba.paw.models.Role;
import ar.edu.itba.paw.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleDao roleDao;

    @Override
    public String getAdminRoleName() {
        return this.roleDao.getAdminRoleName();
    }

    @Override
    public String getUserRoleName() {
        return this.roleDao.getUserRoleName();
    }

    @Override
    public void assignUserRole(long userId) {
        this.roleDao.assignUserRole(userId);
    }

    @Override
    public void assignAdminRole(long userId) {
        this.roleDao.assignAdminRole(userId);
    }

    @Override
    public Collection<String> getUserRoles(User user) {
        return this.roleDao.getUserRoles(user);
    }

    @Override
    public boolean isAdmin(User user) {
        return this.getUserRoles(user).contains(this.getAdminRoleName());
    }
}
