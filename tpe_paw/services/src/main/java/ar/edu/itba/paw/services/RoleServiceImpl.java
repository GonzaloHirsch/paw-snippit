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

    public String getAdminRole() {
        return this.roleDao.getAdminRole();
    }

    @Override
    public String getUserRole() {
        return this.roleDao.getUserRole();
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
    public Collection<String> getUserRoles(long userId) {
        return this.roleDao.getUserRoles(userId);
    }

    @Override
    public boolean isAdmin(long userId) {
        return this.getUserRoles(userId).contains(this.getAdminRole());
    }
}
