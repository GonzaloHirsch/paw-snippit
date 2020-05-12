package ar.edu.itba.paw.interfaces.service;

import ar.edu.itba.paw.models.User;

import java.util.Locale;

public interface EmailService {
    void sendEmail(String to, String subject, String body, Locale locale);

    void sendRegistrationEmail(String to, String username, Locale locale);

    void sendRecoveryEmail(User searchedUser, String baseUrl);

    void sendVerificationEmail(User user);

    void sendFlaggedEmail(String snippetUrl, String snippetTitle, String userEmail, String username, boolean isFlagged, Locale locale);
}
