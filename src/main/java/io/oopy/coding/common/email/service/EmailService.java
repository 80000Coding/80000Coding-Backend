package io.oopy.coding.common.email.service;

import io.oopy.coding.common.email.dto.EmailSend;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * email db를 만들 가능성이 있으므로, service
 */
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;
    private static final String MAIL_CHARACTER_SET = "utf-8";
    private static final String MAIL_SUB_TYPE = "html";

    public void sendMail(EmailSend emailSend) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        message.addRecipients(Message.RecipientType.TO, emailSend.getTargetEmail());
        message.setSubject(emailSend.getTitle());
        message.setText(emailSend.getContent(), MAIL_CHARACTER_SET, MAIL_SUB_TYPE);
        javaMailSender.send(message);
    }
}
