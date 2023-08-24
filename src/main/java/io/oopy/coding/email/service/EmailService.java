package io.oopy.coding.email.service;

import io.oopy.coding.domain.email.entity.EmailCertification;
import io.oopy.coding.domain.email.repository.EmailCertificationRepository;
import io.oopy.coding.email.service.dto.EmailSend;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmailService{

    private final JavaMailSender javaMailSender;
    private final EmailCertificationRepository emailCertificationRepository;

    @Transactional
    public void sendCertMail(EmailSend emailSend) throws MessagingException {
        checkSendValid(emailSend);
        EmailCertification emailCertification =
                EmailCertification.of(emailSend.getTargetEmail(), emailSend.getGithubId());
        // persist first
        emailCertificationRepository.save(emailCertification);
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
        Optional<EmailCertification> email = emailCertificationRepository.findFirstByGithubIdOrderByExpiredAtDesc(emailSend.getGithubId());
        if (email.isPresent() && !email.get().isExpired())
            throw new RuntimeException("인증 진행중");
    }
}
