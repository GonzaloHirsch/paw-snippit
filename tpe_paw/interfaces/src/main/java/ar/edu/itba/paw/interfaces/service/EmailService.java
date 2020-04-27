package ar.edu.itba.paw.interfaces.service;

import java.util.Collection;

public interface EmailService {
    void sendEmail(String to, String subject, String body);

    void sendRegistrationEmail(String to, String username);
}
