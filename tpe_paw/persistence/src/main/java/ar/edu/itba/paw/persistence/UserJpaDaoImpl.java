package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.dao.UserDao;
import ar.edu.itba.paw.models.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Locale;
import java.util.Optional;

@Repository
public class UserJpaDaoImpl implements UserDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public long createUser(String username, String password, String email, String description, int reputation, Timestamp dateJoined, Locale locale) {
        final User user = new User(username, password, email, dateJoined, locale, false);
        this.em.persist(user);
        return user.getId();
    }

    @Override
    public Optional<User> findUserById(Long id) {
        return Optional.ofNullable(this.em.find(User.class, id));
    }

    @Override
    public Optional<User> findUserByUsername(String username) {
        final TypedQuery<User> query = this.em.createQuery("from User as u where u.username = :username", User.class);
        query.setParameter("username", username);
        return query.getResultList().stream().findFirst();
    }

    @Override
    public Optional<User> findUserByEmail(String email) {
        final TypedQuery<User> query = this.em.createQuery("from User as u where u.email = :email", User.class);
        query.setParameter("email", email);
        return query.getResultList().stream().findFirst();
    }

    @Override
    public void updateDescription(String username, String newDescription) {

    }

    @Override
    public void changePassword(String email, String password) {

    }

    @Override
    public void changeProfilePhoto(long userId, byte[] photo) {

    }

    @Override
    public void changeDescription(long userId, String description) {

    }

    @Override
    public void changeReputation(long userId, int value) {

    }

    @Override
    public Collection<User> getAllUsers() {
        return null;
    }

    @Override
    public Collection<User> getAllVerifiedUsers() {
        return null;
    }

    @Override
    public void updateLocale(long userId, Locale locale) {

    }

    @Override
    public String getLocaleLanguage(long userId) {
        return null;
    }

    @Override
    public String getLocaleRegion(long userId) {
        return null;
    }

    @Override
    public boolean userEmailIsVerified(long userId) {
        return false;
    }

    @Override
    public void verifyUserEmail(long userId) {

    }
}
