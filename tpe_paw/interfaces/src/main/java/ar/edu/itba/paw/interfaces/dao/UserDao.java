package ar.edu.itba.paw.interfaces.dao;

import ar.edu.itba.paw.models.User;

import java.util.Date;
import java.util.Optional;

public interface UserDao {

    User createUser(String username, String password, String email, String description, int reputation, Date dateJoined);

    Optional<User> findUserByUsername(String username);

    Optional<User> findUserById(final long id);

    Optional<User> findUserByEmail(String email);

    void updateDescription(String username, String newDescription);

    void changePassword(String email, String password);

    void changeProfilePhoto(final long userId, final byte[] photo);
}
