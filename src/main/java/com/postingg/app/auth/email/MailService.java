package com.postingg.app.auth.email;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class MailService {

    private final JavaMailSender mailSender;
    private final MailContentBuilder mailContentBuilder;

    @Async
    public void sendMail(NotificationMail mail) {
        MimeMessagePreparator messagePrep = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, "utf-8");
            messageHelper.setFrom("authentication@postingg.com");
            messageHelper.setTo(mail.getRecipient());
            messageHelper.setSubject(mail.getSubject());
            messageHelper.setText(mailContentBuilder.build(mail.getBody()));
        };
        try {
            mailSender.send(messagePrep);
            log.info("Activation email sent.");
        } catch (MailException e) {
            log.error("Failed to send email", e);
            // TODO: create custom exception
            throw new IllegalStateException("Failed to send email");
        }
    }

}
