package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;

import java.util.Collection;

public class EmailServiceImpl implements EmailService {
    @Autowired
    public JavaMailSender emailSender;

    @Async
    @Override
    public void sendEmail(String to, String subject, String body) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);
            emailSender.send(message);
        } catch (MailException e){
            // TODO: LOG EMAIL ERROR
        }
    }
}
