package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.dao.UserDao;
import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public User createUser(String username, String password, String email, String description, int reputation, Date dateJoined) {
        return userDao.createUser(username, password, email, description, reputation, dateJoined);
    }

    @Override
    public User register(String username, String password, String email, Date dateJoined) {
        return createUser(username, password, email, "", 0, dateJoined);
    }

    @Override
    public Optional<User> findUserByUsername(String username) {
        return userDao.findUserByUsername(username);
    }

    @Override
    public Optional<User> findUserById(long id) {
        return this.userDao.findUserById(id);
    }

    @Override
    public void updateDescription(String username, String newDescription) {
        userDao.updateDescription(username, newDescription);
    }

    // TODO, should this ask for the current password?
    @Override
    public void changePassword(String email, String password) {
        userDao.changePassword(email, password);
    }

    @Override
    public Optional<User> getCurrentUser() {
        return userDao.getCurrentUser();
    }

    @Override
    public boolean isEmailUnique(String email) {
        return !userDao.findUserByEmail(email).isPresent();
    }

    @Override
    public boolean isUsernameUnique(String username) {
        return !userDao.findUserByUsername(username).isPresent();
    }
}
