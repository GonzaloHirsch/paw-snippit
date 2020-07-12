package ar.edu.itba.paw.interfaces.service;

import ar.edu.itba.paw.models.User;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Collection;
import java.util.Locale;
import java.util.Optional;

public interface UserService {

    User register(String username, String password, String email, Instant dateJoined, Locale locale);

    void registerFollowUp(User user);

    Optional<User> findUserByUsername(String username);

    Optional<User> findUserById(final long id);

    Optional<User> findUserByEmail(String email);

    void changePassword(String email, String password);

    boolean isEmailUnique(String email);

    boolean isUsernameUnique(String username);

    boolean emailExists(String email);

    void changeProfilePhoto(final long userId, final byte[] photo);

    void changeDescription(final long userId, final String description);

    Collection<User> getAllUsers();

    Collection<User> getAllVerifiedUsers();

    void changeReputation(final long userId, final int amount);

    void updateLocale(final long userId, final Locale locale);

    String getLocaleLanguage(final long userId);

    String getLocaleRegion(final long userId);

    boolean userEmailIsVerified(final long userId);

    void verifyUserEmail(final long userId);

}
