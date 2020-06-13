package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.dao.UserDao;
import ar.edu.itba.paw.models.User;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Collection;
import java.util.Locale;
import java.util.Optional;

@Repository
public class UserJpaDaoImpl implements UserDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public User createUser(String username, String password, String email, int reputation, Instant dateJoined, Locale locale) {
        final User user = new User(username, password, email, dateJoined, locale, false);
        this.em.persist(user);
        return user;
    }

    @Override
    public Optional<User> findUserById(Long id) {
        return Optional.ofNullable(this.em.find(User.class, id));
    }

    @Override
    public Optional<User> findUserByUsername(String username) {
        final TypedQuery<User> query = this.em.createQuery("from User as u where u.username = :username", User.class)
                .setParameter("username", username);
        return query.getResultList().stream().findFirst();
    }

    @Override
    public Optional<User> findUserByEmail(String email) {
        final TypedQuery<User> query = this.em.createQuery("from User as u where u.email = :email", User.class)
                .setParameter("email", email);
        return query.getResultList().stream().findFirst();
    }

    @Override
    public void changePassword(String email, String password) {
        Optional<User> maybeUser = this.findUserByEmail(email);
        if(maybeUser.isPresent()){
            User user = maybeUser.get();
            user.setPassword(password);
            this.em.persist(user);
        }
    }

    @Override
    public void changeProfilePhoto(long userId, byte[] photo) {
        Optional<User> maybeUser = this.findUserById(userId);
        if(maybeUser.isPresent()){
            User user = maybeUser.get();
            user.setIcon(photo);
            this.em.persist(user);
        }

    }

    @Override
    public void changeDescription(long userId, String description) {
        Optional<User> maybeUser = this.findUserById(userId);
        if(maybeUser.isPresent()){
            User user = maybeUser.get();
            user.setDescription(description);
            this.em.persist(user);
        }
    }

    @Override
    public void changeReputation(long userId, int value) {
        // blabal
        Optional<User> maybeUser = this.findUserById(userId);
        if(maybeUser.isPresent()){
            User user = maybeUser.get();
            user.setReputation(value);
            this.em.persist(user);
        }
    }

    @Override
    public Collection<User> getAllUsers() {
        return this.em.createQuery("from User", User.class).getResultList();
    }

    //TODO: Check for inconsistency with boolean value of verified
    @Override
    public Collection<User> getAllVerifiedUsers() {
        final TypedQuery<User> query = this.em.createQuery("from User as u where u.verified = true", User.class);
        return query.getResultList();
    }

    @Override
    public void updateLocale(long userId, Locale locale) {
        Optional<User> maybeUser = this.findUserById(userId);
        if(maybeUser.isPresent()){
            User user = maybeUser.get();
            user.setRegion(locale.getCountry());
            user.setLang(locale.getLanguage());
            this.em.persist(user);
        }
    }

    @Override
    public String getLocaleLanguage(long userId) {
        Optional<User> maybeUser = this.findUserById(userId);
        if(maybeUser.isPresent()){
            User user = maybeUser.get();
            return user.getLang();
        }
        return "";
    }

    @Override
    public String getLocaleRegion(long userId) {
        Optional<User> maybeUser = this.findUserById(userId);
        if(maybeUser.isPresent()){
            User user = maybeUser.get();
            return user.getRegion();
        }
        return "";
    }

    //TODO: Check for inconsistency with boolean value of verified
    @Override
    public boolean userEmailIsVerified(long userId) {
        Optional<User> maybeUser = this.findUserById(userId);
        if(maybeUser.isPresent()){
            User user = maybeUser.get();
            return user.isVerified();
        }
        return false;
    }

    //TODO: Check for inconsistency with boolean value of verified
    @Override
    public void verifyUserEmail(long userId) {
        Optional<User> maybeUser = this.findUserById(userId);
        if(maybeUser.isPresent()){
            User user = maybeUser.get();
            user.setVerified(true);
            this.em.persist(user);
        }
    }
}
