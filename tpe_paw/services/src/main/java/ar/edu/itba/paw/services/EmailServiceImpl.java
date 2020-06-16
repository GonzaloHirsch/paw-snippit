package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.service.*;
import ar.edu.itba.paw.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Service
public class EmailServiceImpl implements EmailService {
    @Autowired
    public JavaMailSender emailSender;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private TemplateService templateService;
    @Autowired
    private CryptoService cryptoService;

    private void sendEmail(String to, String subject, String body, Locale locale) {
        try {
            MimeMessage message = this.emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setSubject(subject);
            // True flag to inform the helper it's html
            helper.setText(body, true);
            helper.setFrom(messageSource.getMessage("app.name", null, locale));
            emailSender.send(message);
        } catch (MailException | MessagingException e) {
            throw new RuntimeException("[MAIL EXCEPTION] [Message:  " + e.getMessage() + "]");
        }
    }

    @Async
    @Override
    public void sendRegistrationEmail(String to, String username, Locale locale) {
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("username", username);
        String body = this.templateService.merge("/templates/register.vm", data, locale);
        String subject = messageSource.getMessage("email.register.subject", new Object[]{username}, locale);
        this.sendEmail(to, subject, body, locale);
    }

    @Async
    @Override
    public void sendRecoveryEmail(User searchedUser, String baseUrl) {
        String otp = this.cryptoService.generateTOTP(searchedUser.getEmail(), searchedUser.getPassword());
        String token = this.cryptoService.generateRecoverToken(otp);
        String link = baseUrl + "/reset-password?id=" + searchedUser.getId() + "&token=" + token;
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("recoveryURL", link);
        data.put("username", searchedUser.getUsername());
        data.put("userEmail", searchedUser.getEmail());
        String body = this.templateService.merge("/templates/passwordRecovery.vm", data, searchedUser.getLocale());
        String subject = messageSource.getMessage("email.recovery.subject", null, searchedUser.getLocale());
        this.sendEmail(searchedUser.getEmail(), subject, body, searchedUser.getLocale());
    }

    @Async
    @Override
    public void sendVerificationEmail(User user){
        String otp = this.cryptoService.generateTOTP(user.getEmail(), user.getPassword());
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("code", otp);
        data.put("username", user.getUsername());
        data.put("userEmail", user.getEmail());
        String body = this.templateService.merge("/templates/emailVerification.vm", data, user.getLocale());
        String subject = messageSource.getMessage("email.verification.subject", null, user.getLocale());
        this.sendEmail(user.getEmail(), subject, body, user.getLocale());
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
    @Override
    public void sendDigestEmail(String to, String username, int count, Locale locale) {
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("itemCount", count);
        data.put("username", username);
        String body = this.templateService.merge("/templates/weeklyDigest.vm", data, locale);
        String subject = messageSource.getMessage("email.wd.subject", null, locale);
        this.sendEmail(to, subject, body, locale);
    }

    @Async
    @Override
    public void sendDigestNoFollowEmail(String to, String username, Locale locale) {
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("username", username);
        String body = this.templateService.merge("/templates/weeklyDigestNoItems.vm", data, locale);
        String subject = messageSource.getMessage("email.wdni.subject", null, locale);
        this.sendEmail(to, subject, body, locale);
    }

    @Async
    @Override
    public void sendDigestFollowOtherEmail(String to, String username, Locale locale) {
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("username", username);
        String body = this.templateService.merge("/templates/weeklyDigestSuggestFollowing.vm", data, locale);
        String subject = messageSource.getMessage("email.wdsf.subject", null, locale);
        this.sendEmail(to, subject, body, locale);
    }

    @Async
    @Override
    public void sendReportedEmail(String snippetUrl, String snippetTitle, String userEmail, String username, String reportDetail, String reportingUserName, Locale locale){
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("snippetUrl", snippetUrl);
        data.put("username", username);
        data.put("title", snippetTitle);
        data.put("reportDetail", reportDetail);
        data.put("reportingUserName", reportingUserName);
        String body;
        body = this.templateService.merge("/templates/reportedSnippet.vm", data, locale);
        String subject = messageSource.getMessage("email.reported.subject", null, locale);
        this.sendEmail(userEmail, subject, body, locale);
    }
}
