package ar.edu.itba.paw.interfaces.service;

import java.util.Locale;

public interface EmailService {
    void sendEmail(String to, String subject, String body, Locale locale);

    void sendRegistrationEmail(String to, String username, Locale locale);

    void sendRecoveryEmail(String baseUrl, String userEmail, Locale locale);

    void scheduledWeeklyDigest();

    void sendDigestEmail(String to, String username, int count, Locale locale);

    void sendDigestNoFollowEmail(String to, String username, Locale locale);

    void sendDigestFollowOtherEmail(String to, String username, Locale locale);
}
