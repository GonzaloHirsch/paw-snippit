package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.service.*;
import ar.edu.itba.paw.models.Tag;
import ar.edu.itba.paw.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Autowired private TagService tagService;
    @Autowired private SnippetService snippetService;

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
                    this.emailService.sendDigestEmail(user.getEmail(), user.getUsername(), snippetsForWeek, locale);
                } else {
                    this.emailService.sendDigestFollowOtherEmail(user.getEmail(), user.getUsername(), locale);
                }
            } else {
                this.emailService.sendDigestNoFollowEmail(user.getEmail(), user.getUsername(), locale);
            }
        }
    }
}
