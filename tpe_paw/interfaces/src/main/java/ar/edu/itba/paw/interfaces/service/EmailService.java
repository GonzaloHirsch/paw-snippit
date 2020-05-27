package ar.edu.itba.paw.interfaces.service;

import ar.edu.itba.paw.models.User;

import java.util.Locale;

public interface EmailService {
    void sendRegistrationEmail(String to, String username, Locale locale);

    void sendRecoveryEmail(User searchedUser, String baseUrl);

    void sendVerificationEmail(User user);

    void sendFlaggedEmail(String snippetUrl, String snippetTitle, String userEmail, String username, boolean isFlagged, Locale locale);

    void sendDigestEmail(String to, String username, int count, Locale locale);

    void sendDigestNoFollowEmail(String to, String username, Locale locale);

    void sendDigestFollowOtherEmail(String to, String username, Locale locale);
}
