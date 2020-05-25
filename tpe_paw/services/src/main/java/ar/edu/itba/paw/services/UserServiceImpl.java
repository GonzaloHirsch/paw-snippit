package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.dao.UserDao;
import ar.edu.itba.paw.interfaces.service.EmailService;
import ar.edu.itba.paw.interfaces.service.RoleService;
import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Locale;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired private UserDao userDao;
    @Autowired private RoleService roleService;
    @Autowired private EmailService emailService;

    @Transactional
    @Override
    public long createUser(String username, String password, String email, int reputation, Timestamp dateJoined, Locale locale) {
        return this.userDao.createUser(username, password, email, reputation, dateJoined, locale);
    }

    @Transactional
    @Override
    public long register(String username, String password, String email, Timestamp dateJoined, Locale locale) {
        long userId = createUser(username, password, email, 0, dateJoined, locale);
        this.roleService.assignUserRole(userId);
        this.emailService.sendRegistrationEmail(email, username, LocaleContextHolder.getLocale());
        return userId;
    }

    @Override
    public Optional<User> findUserByUsername(String username) {
        return this.userDao.findUserByUsername(username);
    }

    @Override
    public Optional<User> findUserById(long id) {
        return this.userDao.findUserById(id);
    }

    @Override
    public Optional<User> findUserByEmail(String email) {
        return this.userDao.findUserByEmail(email);
    }

    @Transactional
    @Override
    public void changePassword(String email, String password) {
        this.userDao.changePassword(email, password);
    }

    @Override
    public boolean isEmailUnique(String email) {
        return !this.userDao.findUserByEmail(email).isPresent();
    }

    @Override
    public boolean isUsernameUnique(String username) {
        return !this.userDao.findUserByUsername(username).isPresent();
    }

    @Override
    public boolean emailExists(String email) {
        return this.userDao.findUserByEmail(email).isPresent();
    }

    @Transactional
    @Override
    public void changeProfilePhoto(long userId, byte[] photo) {
        this.userDao.changeProfilePhoto(userId, photo);
    }

    @Transactional
    @Override
    public void changeDescription(final long userId, final String description) {
        this.userDao.changeDescription(userId, description);
    }

    @Transactional
    @Override
    public void changeReputation(long userId, int amount) {
        this.userDao.changeReputation(userId, amount);
    }

    @Transactional
    @Override
    public void updateLocale(long userId, Locale locale) {
        String language = this.userDao.getLocaleLanguage(userId);
        String region = this.userDao.getLocaleRegion(userId);

        if (!locale.getLanguage().equals(language) || !locale.getCountry().equals(region)) {
            this.userDao.updateLocale(userId, locale);
        }
    }

    @Override
    public String getLocaleLanguage(long userId) {
        return this.userDao.getLocaleLanguage(userId);
    }

    @Override
    public String getLocaleRegion(long userId) {
        return this.userDao.getLocaleRegion(userId);
    }

    @Override
    public boolean userEmailIsVerified(long userId) {
        return this.userDao.userEmailIsVerified(userId);
    }

    @Override
    public void verifyUserEmail(long userId) {
        this.userDao.verifyUserEmail(userId);
    }

    @Override
    public Collection<User> getAllUsers() {
        return this.userDao.getAllUsers();
    }

    @Override
    public Collection<User> getAllVerifiedUsers() {
        return this.userDao.getAllVerifiedUsers();
    }
}
