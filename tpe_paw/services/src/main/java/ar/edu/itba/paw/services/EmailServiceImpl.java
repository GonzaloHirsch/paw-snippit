package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

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
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);
            message.setFrom(messageSource.getMessage("app.name", null, LocaleContextHolder.getLocale()));
            emailSender.send(message);
        } catch (MailException e){
            // TODO: LOG EMAIL ERROR
        }
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
    public void sendRecoveryEmail(String to, String username, String token) {
//        String link = "http://localhost:9092/"
        String subject = messageSource.getMessage("email.recovery.subject", null, LocaleContextHolder.getLocale());
        String body = messageSource.getMessage("email.recovery.body", new Object[]{username, token}, LocaleContextHolder.getLocale());
        this.sendEmail(to, subject, body);
    }
}
