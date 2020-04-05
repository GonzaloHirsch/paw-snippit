package ar.edu.itba.paw.interfaces.dao;

import ar.edu.itba.paw.models.User;

import java.util.Date;

public interface UserDao {
    User createUser(String username, String password, String email, String description, Date dateJoined);

    User findUserByUsername(String username);

    void updateDescription(String username, String newDescription);

    void changePassword(String email, String password);
}
