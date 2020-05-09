package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.service.*;
import ar.edu.itba.paw.models.Tag;
import ar.edu.itba.paw.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletContext;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class EmailServiceImpl implements EmailService {
    @Autowired
    public JavaMailSender emailSender;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private UserService userService;
    @Autowired
    private SnippetService snippetService;
    @Autowired
    private TagService tagService;
    @Autowired
    private TemplateService templateService;
    @Autowired
    private CryptoService cryptoService;
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    @Async
    @Override
    public void sendEmail(String to, String subject, String body, Locale locale) {
        try {
            MimeMessage message = this.emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setSubject(subject);
            // True flag to inform the helper it's html
            helper.setText(body, true);
            helper.setFrom(messageSource.getMessage("app.name", null, locale));
            emailSender.send(message);
        } catch (MailException e) {
            // TODO: LOG EMAIL ERROR
        } catch (MessagingException e) {
            // TODO: LOG ERROR
        }
    }

    @Async
    @Override
    public void sendRegistrationEmail(String to, String username, Locale locale) {
        try {
            Map<String, Object> data = new HashMap<String, Object>();
            data.put("username", username);
            String body = this.templateService.merge("/templates/register.vm", data, locale);
            String subject = messageSource.getMessage("email.register.subject", new Object[]{username}, locale);
            this.sendEmail(to, subject, body, locale);
        } catch (Exception e) {
            // TODO: DO SMTH
        }
    }

    @Async
    @Override
    public void sendRecoveryEmail(String baseUrl, String userEmail) {
        User searchedUser = userService.findUserByEmail(userEmail).get(); // User SHOULD be found
        String base64Token = cryptoService.generateTOTP(userEmail, searchedUser.getPassword());
        String link = baseUrl + "/reset-password?id=" + searchedUser.getId() + "&token=" + base64Token;
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("recoveryURL", link);
        data.put("username", searchedUser.getUsername());
        data.put("userEmail", searchedUser.getEmail());
        String body = this.templateService.merge("/templates/passwordRecovery.vm", data, searchedUser.getLocale());
        String subject = messageSource.getMessage("email.recovery.subject", null, searchedUser.getLocale());
        this.sendEmail(userEmail, subject, body, searchedUser.getLocale());
    }

    @Async
    @Override
    public void sendFlaggedEmail(String snippetUrl, String snippetTitle, String userEmail, String username, boolean isFlagged, Locale locale){
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("snippetUrl", snippetUrl);
        data.put("username", username);
        data.put("title", snippetTitle);
        String body;
        if (isFlagged){
            body = this.templateService.merge("/templates/flaggedSnippet.vm", data, locale);
        } else {
            body = this.templateService.merge("/templates/notFlaggedSnippet.vm", data, locale);
        }
        String subject = messageSource.getMessage("email.flagged.subject", null, locale);
        this.sendEmail(userEmail, subject, body, locale);
    }

    @Async
    @Scheduled(cron = "0 0 12 * * Mon")
    @Override
    public void scheduledWeeklyDigest() {
        // Getting timestamp for week before
        Calendar weekBefore = Calendar.getInstance();
        weekBefore.add(Calendar.WEEK_OF_YEAR, -1);
        Timestamp weekBeforeTs = new Timestamp(weekBefore.getTime().getTime());
        // Getting all users
        Collection<User> users = this.userService.getAllUsers();
        Collection<Tag> followedTags;
        int snippetsForWeek;
        Locale locale;
        for (User user : users) {
            locale = user.getLocale();
            // Getting all followed tags
            followedTags = this.tagService.getFollowedTagsForUser(user.getId());
            if (followedTags.size() > 0) {
                // Getting how many new snippets were found
                snippetsForWeek = this.snippetService.getNewSnippetsForTagsCount(sdf.format(weekBeforeTs), followedTags, user.getId());
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
            this.sendEmail(to, subject, body, locale);
        } catch (Exception e) {
            // TODO: DO SMTH
            String a = e.getMessage();
        }
    }

    @Override
    public void sendDigestNoFollowEmail(String to, String username, Locale locale) {
        try {
            Map<String, Object> data = new HashMap<String, Object>();
            data.put("username", username);
            String body = this.templateService.merge("/templates/weeklyDigestNoItems.vm", data, locale);
            String subject = messageSource.getMessage("email.wdni.subject", null, locale);
            this.sendEmail(to, subject, body, locale);
        } catch (Exception e) {
            // TODO: DO SMTH
            String a = e.getMessage();
        }
    }

    @Override
    public void sendDigestFollowOtherEmail(String to, String username, Locale locale) {
        try {
            Map<String, Object> data = new HashMap<String, Object>();
            data.put("username", username);
            String body = this.templateService.merge("/templates/weeklyDigestSuggestFollowing.vm", data, locale);
            String subject = messageSource.getMessage("email.wdsf.subject", null, locale);
            this.sendEmail(to, subject, body, locale);
        } catch (Exception e) {
            // TODO: DO SMTH
            String a = e.getMessage();
        }
    }


}
