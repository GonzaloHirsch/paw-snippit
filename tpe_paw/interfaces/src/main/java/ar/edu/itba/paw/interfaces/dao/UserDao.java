package ar.edu.itba.paw.interfaces.dao;

import ar.edu.itba.paw.models.User;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Locale;
import java.util.Optional;

public interface UserDao {

    long createUser(String username, String password, String email, String description, int reputation, Timestamp dateJoined, Locale locale);

    Optional<User> findUserByUsername(String username);

    Optional<User> findUserById(final Long id);

    Optional<User> findUserByEmail(String email);

    void changePassword(String email, String password);

    void changeProfilePhoto(final long userId, final byte[] photo);

    void changeDescription(final long userId, final String description);

    void changeReputation(long userId, int value);

    Collection<User> getAllUsers();

    Collection<User> getAllVerifiedUsers();

    void updateLocale(final long userId, final Locale locale);

    String getLocaleLanguage(final long userId);

    String getLocaleRegion(final long userId);

    boolean userEmailIsVerified(final long userId);

    void verifyUserEmail(final long userId);
}
