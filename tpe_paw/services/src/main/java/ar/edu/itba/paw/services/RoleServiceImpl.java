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

    public Role getAdminRole() {
        return this.roleDao.getAdminRole();
    }

    @Override
    public Role getUserRole() {
        return this.roleDao.getUserRole();
    }

    @Override
    public void assignUserRole(User user) {
        user.addRole(this.roleDao.assignUserRole(user.getId()));
    }

    @Override
    public void assignAdminRole(User user) {
        user.addRole(this.roleDao.assignAdminRole(user.getId()));
    }

    @Override
    public Collection<String> getUserRoles(long userId) {
        return this.roleDao.getUserRoles(userId);
    }
}
