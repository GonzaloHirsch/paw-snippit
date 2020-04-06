package ar.edu.itba.paw.interfaces.service;

import ar.edu.itba.paw.models.User;

import java.util.Date;
import java.util.Optional;

public interface UserService {
    User createUser(String username, String password, String email, String description, Date dateJoined);

    Optional<User> findUserByUsername(String username);

    void updateDescription(String username, String newDescription);

    void changePassword(String email, String password);

    Optional<User> getCurrentUser();
}
