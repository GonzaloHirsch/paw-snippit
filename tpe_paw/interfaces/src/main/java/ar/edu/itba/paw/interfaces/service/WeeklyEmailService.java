package ar.edu.itba.paw.interfaces.service;

import java.util.Locale;

public interface WeeklyEmailService {
    void scheduledWeeklyDigest();

    void sendDigestEmail(String to, String username, int count, Locale locale);

    void sendDigestNoFollowEmail(String to, String username, Locale locale);

    void sendDigestFollowOtherEmail(String to, String username, Locale locale);
}
