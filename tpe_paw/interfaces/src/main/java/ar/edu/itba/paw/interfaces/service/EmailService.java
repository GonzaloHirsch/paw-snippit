package ar.edu.itba.paw.interfaces.service;

public interface EmailService {
    void sendEmail(String to, String subject, String body);

    void sendRegistrationEmail(String to, String username);

    void sendRecoveryEmail(long userId, String to, String username, String token);

    void sendDailyDigest();
}
