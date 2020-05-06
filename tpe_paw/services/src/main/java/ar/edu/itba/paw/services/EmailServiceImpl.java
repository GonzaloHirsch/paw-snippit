package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.service.*;
import ar.edu.itba.paw.models.Tag;
import ar.edu.itba.paw.models.User;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

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
    public void sendEmail(String to, String subject, String body) {
        try {
            MimeMessage message = this.emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setSubject(subject);
            // True flag to inform the helper it's html
            helper.setText(body, true);
            helper.setFrom(messageSource.getMessage("app.name", null, LocaleContextHolder.getLocale()));
            emailSender.send(message);
        } catch (MailException e) {
            // TODO: LOG EMAIL ERROR
        } catch (MessagingException e) {
            // TODO: LOG ERROR
        }
    }

    @Async
    @Override
    public void sendRegistrationEmail(String to, String username) {
        try {
            Map<String, Object> data = new HashMap<String, Object>();
            data.put("username", username);
            String body = this.templateService.merge("/templates/register.vm", data, LocaleContextHolder.getLocale());
            String subject = messageSource.getMessage("email.register.subject", new Object[]{username}, LocaleContextHolder.getLocale());
            this.sendEmail(to, subject, body);
        } catch (Exception e) {
            // TODO: DO SMTH
            String a = e.getMessage();
        }
    }
    @Async
    @Override
    public void sendRecoveryEmail(String userEmail) {
        User searchedUser = userService.findUserByEmail(userEmail).get(); // User SHOULD be found
        String base64Token = cryptoService.generateTOTP(userEmail, searchedUser.getPassword());
        String link = "http://localhost:9092/webapp_war_exploded/reset-password?id=" + searchedUser.getId() + "&token=" + base64Token;
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("recoveryURL", link);
        data.put("username", searchedUser.getUsername());
        data.put("userEmail", searchedUser.getEmail());
        String body = this.templateService.merge("/templates/passwordRecovery.vm", data, LocaleContextHolder.getLocale());
        String subject = messageSource.getMessage("email.recovery.subject", null, LocaleContextHolder.getLocale());
        this.sendEmail(userEmail, subject, body);
    }

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
        for (User user : users) {
            // Getting all followed tags
            followedTags = this.tagService.getFollowedTagsForUser(user.getId());
            if (followedTags.size() > 0) {
                // Getting how many new snippets were found
                snippetsForWeek = this.snippetService.getNewSnippetsForTagsCount(sdf.format(weekBeforeTs), followedTags, user.getId());
                if (snippetsForWeek > 0) {
                    this.sendDigestEmail(user.getEmail(), user.getUsername(), snippetsForWeek);
                } else {
                    this.sendDigestFollowOtherEmail(user.getEmail(), user.getUsername());
                }
            } else {
                this.sendDigestNoFollowEmail(user.getEmail(), user.getUsername());
            }
        }
    }

    @Override
    public void sendDigestEmail(String to, String username, int count) {
        try {
            Map<String, Object> data = new HashMap<String, Object>();
            data.put("itemCount", count);
            data.put("username", username);
            String body = this.templateService.merge("/templates/weeklyDigest.vm", data, LocaleContextHolder.getLocale());
            String subject = messageSource.getMessage("email.wd.subject", null, LocaleContextHolder.getLocale());
            this.sendEmail(to, subject, body);
        } catch (Exception e) {
            // TODO: DO SMTH
            String a = e.getMessage();
        }
    }

    @Override
    public void sendDigestNoFollowEmail(String to, String username) {
        try {
            Map<String, Object> data = new HashMap<String, Object>();
            data.put("username", username);
            String body = this.templateService.merge("/templates/weeklyDigestNoItmes.vm", data, LocaleContextHolder.getLocale());
            String subject = messageSource.getMessage("email.wdni.subject", null, LocaleContextHolder.getLocale());
            this.sendEmail(to, subject, body);
        } catch (Exception e) {
            // TODO: DO SMTH
            String a = e.getMessage();
        }
    }

    @Override
    public void sendDigestFollowOtherEmail(String to, String username) {
        try {
            Map<String, Object> data = new HashMap<String, Object>();
            data.put("username", username);
            String body = this.templateService.merge("/templates/weeklyDigestSuggestFollowing.vm", data, LocaleContextHolder.getLocale());
            String subject = messageSource.getMessage("email.wdsf.subject", null, LocaleContextHolder.getLocale());
            this.sendEmail(to, subject, body);
        } catch (Exception e) {
            // TODO: DO SMTH
            String a = e.getMessage();
        }
    }


}
