package io.oopy.coding.email.service;

import io.oopy.coding.common.redis.emailcert.EmailCertification;
import io.oopy.coding.common.redis.emailcert.EmailCertificationService;
import io.oopy.coding.common.redis.emailcert.dto.EmailCert;
import io.oopy.coding.email.service.dto.EmailCertificate;
import io.oopy.coding.email.service.dto.EmailSend;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.UUID;

import static io.oopy.coding.common.constant.EmailConstant.CERT_CONTENT;

@Service
@RequiredArgsConstructor
public class EmailService{

    @Value("${mail.cert.domain}")
    private String emailCertDomain;

    private static final String MAIL_CHARACTER_SET = "utf-8";
    private static final String MAIL_SUB_TYPE = "html";

    private final JavaMailSender javaMailSender;
    private final EmailCertificationService emailCertificationService;

    @Transactional
    public void sendCertMail(EmailCertificate.Send emailSend) throws MessagingException {
        checkSendValid(emailSend);
        String token = UUID.randomUUID().toString();
        emailSend.setContent(String.format(CERT_CONTENT,
                emailCertDomain,
                emailSend.getGithubId(),
                token
        ));
        emailCertificationService.register(new EmailCert.CreateUpdate(emailSend.getGithubId(), token));
        sendMail(emailSend);
    }

    @Transactional
    public void deleteCert(String githubId) {
        emailCertificationService.delete(new EmailCert.ReadDelete(githubId));
    }

    @Transactional
    public void cert(String githubId, String token) {
       emailCertificationService.cert(new EmailCert.CreateUpdate(githubId, token));
    }

    private void sendMail(EmailSend emailSend) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        message.addRecipients(Message.RecipientType.TO, emailSend.getTargetEmail());
        message.setSubject(emailSend.getTitle());
        message.setText(emailSend.getContent(), MAIL_CHARACTER_SET, MAIL_SUB_TYPE);
        javaMailSender.send(message);
    }

    private void checkSendValid(EmailCertificate.Send emailSend) {
        EmailCertification emailCertification = emailCertificationService.getEmailCertification(EmailCert.ReadDelete.of(emailSend.getGithubId()));
        if (!ObjectUtils.isEmpty(emailCertification)) {
            throw new RuntimeException("인증 진행중");
        }
    }
}
