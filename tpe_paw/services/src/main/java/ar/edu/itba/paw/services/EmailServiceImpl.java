package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Collection;

@Service
public class EmailServiceImpl implements EmailService {
    @Autowired
    public JavaMailSender emailSender;
    @Autowired
    private MessageSource messageSource;

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
        } catch (MailException e){
            // TODO: LOG EMAIL ERROR
        } catch (MessagingException e){
            // TODO: LOG ERROR
        }
    }

    @Async
    @Override
    public void sendRegistrationEmail(String to, String username) {
        try {
            String subject = messageSource.getMessage("email.register.subject",null, LocaleContextHolder.getLocale());
            String body = messageSource.getMessage("email.register.body",new Object[]{username}, LocaleContextHolder.getLocale());
            this.sendEmail(to, subject, body);
        } catch (Exception e){
            // TODO: DO SMTH
            String a = e.getMessage();
        }
    }
}
