package ar.edu.itba.paw.interfaces.dao;

import ar.edu.itba.paw.models.User;

import java.util.Date;
import java.util.Optional;

public interface UserDao {
    User createUser(String username, String password, String email, String description, Date dateJoined);

    Optional<User> findUserByUsername(String username);

    Optional<User> findUserById(long id);

    void updateDescription(String username, String newDescription);

    void changePassword(String email, String password);
}
