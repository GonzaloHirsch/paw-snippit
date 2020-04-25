package ar.edu.itba.paw.interfaces.service;

import ar.edu.itba.paw.models.User;

import java.util.Date;
import java.util.Optional;

public interface UserService {
    User createUser(String username, String password, String email, String description, int reputation, Date dateJoined);

    User register(String username, String password, String email, Date dateJoined);

    Optional<User> findUserByUsername(String username);

    Optional<User> findUserById(final long id);

    void updateDescription(String username, String newDescription);

    void changePassword(String email, String password);

    Optional<User> getCurrentUser();

    boolean isEmailUnique(String email);

    boolean isUsernameUnique(String username);
}
