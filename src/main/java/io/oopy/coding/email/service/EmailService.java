package io.oopy.coding.email.service;

import io.oopy.coding.common.redis.email.EmailCertificationService;
import io.oopy.coding.email.service.dto.EmailSend;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmailService{

    private final JavaMailSender javaMailSender;
    private final EmailCertificationService emailCertificationService;

    @Transactional
    public void sendCertMail(EmailSend emailSend) throws MessagingException {
        checkSendValid(emailSend);
        emailCertificationService.register(UUID.randomUUID().toString(), emailSend.getGithubId());
        sendMail(emailSend);
    }

    private void sendMail(EmailSend emailSend) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        message.addRecipients(Message.RecipientType.TO, emailSend.getTargetEmail());
        message.setSubject(emailSend.getTitle());
        message.setText(emailSend.getContent(), "utf-8", "html");
        javaMailSender.send(message);
    }

    private void checkSendValid(EmailSend emailSend) {
        String githubId = emailCertificationService.getGithubId(emailSend.getGithubId());
        if (!ObjectUtils.isEmpty(githubId))
            throw new RuntimeException("인증 진행중");
    }
}
