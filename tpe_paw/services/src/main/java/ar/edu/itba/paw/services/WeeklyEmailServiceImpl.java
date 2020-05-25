package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.service.*;
import ar.edu.itba.paw.models.Tag;
import ar.edu.itba.paw.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


@Service
public class WeeklyEmailServiceImpl implements WeeklyEmailService{

    @Autowired private UserService userService;
    @Autowired private EmailService emailService;
    @Autowired private MessageSource messageSource;
    @Autowired private TagService tagService;
    @Autowired private SnippetService snippetService;
    @Autowired private TemplateService templateService;

    @Async
    @Scheduled(cron = "0 0 12 * * Mon")
    @Override
    public void scheduledWeeklyDigest() {
        // Getting timestamp for week before
        Instant weekBefore = Instant.now().plus(-7, ChronoUnit.DAYS);
        // Getting all verified users
        Collection<User> users = this.userService.getAllVerifiedUsers();
        Collection<Tag> followedTags;
        int snippetsForWeek;
        Locale locale;
        for (User user : users) {
            locale = user.getLocale();
            // Getting all followed tags
            followedTags = this.tagService.getFollowedTagsForUser(user.getId());
            if (followedTags.size() > 0) {
                // Getting how many new snippets were found
                snippetsForWeek = this.snippetService.getNewSnippetsForTagsCount(weekBefore, followedTags, user.getId());
                if (snippetsForWeek > 0) {
                    this.sendDigestEmail(user.getEmail(), user.getUsername(), snippetsForWeek, locale);
                } else {
                    this.sendDigestFollowOtherEmail(user.getEmail(), user.getUsername(), locale);
                }
            } else {
                this.sendDigestNoFollowEmail(user.getEmail(), user.getUsername(), locale);
            }
        }
    }

    @Override
    public void sendDigestEmail(String to, String username, int count, Locale locale) {
        try {
            Map<String, Object> data = new HashMap<String, Object>();
            data.put("itemCount", count);
            data.put("username", username);
            String body = this.templateService.merge("/templates/weeklyDigest.vm", data, locale);
            String subject = messageSource.getMessage("email.wd.subject", null, locale);
            this.emailService.sendEmail(to, subject, body, locale);
        } catch (Exception e) {

        }
    }

    @Override
    public void sendDigestNoFollowEmail(String to, String username, Locale locale) {
        try {
            Map<String, Object> data = new HashMap<String, Object>();
            data.put("username", username);
            String body = this.templateService.merge("/templates/weeklyDigestNoItems.vm", data, locale);
            String subject = messageSource.getMessage("email.wdni.subject", null, locale);
            this.emailService.sendEmail(to, subject, body, locale);
        } catch (Exception e) {

        }
    }

    @Override
    public void sendDigestFollowOtherEmail(String to, String username, Locale locale) {
        try {
            Map<String, Object> data = new HashMap<String, Object>();
            data.put("username", username);
            String body = this.templateService.merge("/templates/weeklyDigestSuggestFollowing.vm", data, locale);
            String subject = messageSource.getMessage("email.wdsf.subject", null, locale);
            this.emailService.sendEmail(to, subject, body, locale);
        } catch (Exception e) {

        }
    }

}
