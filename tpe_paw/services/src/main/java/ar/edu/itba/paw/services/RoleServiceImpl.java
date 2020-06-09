package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.dao.RoleDao;
import ar.edu.itba.paw.interfaces.service.RoleService;
import ar.edu.itba.paw.models.Role;
import ar.edu.itba.paw.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    @Override
    public boolean assignUserRole(long userId) {
        return this.roleDao.assignUserRole(userId);
    }

    @Transactional
    @Override
    public boolean assignAdminRole(long userId) {
        return this.roleDao.assignAdminRole(userId);
    }

    @Override
    public Collection<String> getUserRoles(long userId) {
        return this.roleDao.getUserRoles(userId);
    }

    @Override
    public boolean isAdmin(long userId) {
        return this.getUserRoles(userId).contains(this.getAdminRoleName());
    }
}
