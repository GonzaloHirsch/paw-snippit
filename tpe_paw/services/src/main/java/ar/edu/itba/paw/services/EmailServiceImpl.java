package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.service.EmailService;
import ar.edu.itba.paw.interfaces.service.SnippetService;
import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.models.Tag;
import ar.edu.itba.paw.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

@Service
public class EmailServiceImpl implements EmailService {
    @Autowired public JavaMailSender emailSender;
    @Autowired private MessageSource messageSource;
    @Autowired private UserService userService;
    @Autowired private SnippetService snippetService;
    @Autowired private TagServiceImpl tagService;

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    @Async
    @Override
    public void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        message.setFrom(messageSource.getMessage("app.name", null, LocaleContextHolder.getLocale()));
        emailSender.send(message);
    }

    @Async
    @Override
    public void sendRegistrationEmail(String to, String username) {
        String subject = messageSource.getMessage("email.register.subject",null, LocaleContextHolder.getLocale());
        String body = messageSource.getMessage("email.register.body",new Object[]{username}, LocaleContextHolder.getLocale());
        this.sendEmail(to, subject, body);
    }
    @Async
    @Override
    public void sendRecoveryEmail(long id, String to, String username, String token) {
        String link = "http://localhost:9092/webapp_war_exploded/reset-password?id=" + id + "&token=" + token;
        String subject = messageSource.getMessage("email.recovery.subject", null, LocaleContextHolder.getLocale());
        String body = messageSource.getMessage("email.recovery.body", new Object[]{username, link}, LocaleContextHolder.getLocale());
        this.sendEmail(to, subject, body);
    }

    @Scheduled(cron = "0 0 12 * * Mon")
    @Override
    public void sendDailyDigest() {
        // Getting timestamp for week before
        Calendar weekBefore = Calendar.getInstance();
        weekBefore.add(Calendar.WEEK_OF_YEAR, -1);
        Timestamp weekBeforeTs = new Timestamp(weekBefore.getTime().getTime());
        // Getting all users
        Collection<User> users = this.userService.getAllUsers();
        Collection<Tag> followedTags;
        int snippetsForDay;
        for (User user : users){
            // Getting all followed tags
            followedTags = this.tagService.getFollowedTagsForUser(user.getId());
            if (followedTags.size() > 0){
                // Getting how many new snippets were found
                snippetsForDay = this.snippetService.getNewSnippetsForTagsCount(sdf.format(weekBeforeTs), followedTags, user.getId());
                if (snippetsForDay > 0){
                    this.sendEmail(user.getEmail(), "Daily Digest" , "This is your daily digest, " + snippetsForDay + " snippets were added! Come check them out!");
                }
            }
        }
    }
}
