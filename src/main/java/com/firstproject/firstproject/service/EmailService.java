package com.firstproject.firstproject.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
//@Slf4j // used to log
public class EmailService {

    // accessing javaMailSender bean
    @Autowired
    private JavaMailSender MailSender;

    public void sendMail(String to, String subject, String body) {
        try{
            SimpleMailMessage mail = new SimpleMailMessage();
            mail.setTo(to);
            mail.setSubject(subject);
            mail.setText(body);

            MailSender.send(mail);
            System.out.println("mail sent successfully to " + to);
        } catch(Exception e) {
//          log.error("Error Sending mail : ", e);
            e.printStackTrace();
        }

    }
}
