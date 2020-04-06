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
    public User createUser(String username, String password, String email, String description, Date dateJoined) {
        return userDao.createUser(username, password, email, description, dateJoined);
    }

    @Override
    public Optional<User> findUserByUsername(String username) {
        return userDao.findUserByUsername(username);
    }

    @Override
    public void updateDescription(String username, String newDescription) {
        userDao.updateDescription(username, newDescription);
    }

    @Override
    public void changePassword(String email, String password) {
        userDao.changePassword(email, password);
    }

    @Override
    public Optional<User> getCurrentUser() {
        return userDao.getCurrentUser();
    }
}
