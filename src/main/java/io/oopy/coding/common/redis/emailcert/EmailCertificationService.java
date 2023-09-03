package io.oopy.coding.common.redis.emailcert;

import io.oopy.coding.common.redis.emailcert.dto.EmailCert;
import io.oopy.coding.common.redis.emailcert.dto.EmailCertKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.Optional;

import static io.oopy.coding.common.constant.EmailConstant.EMAIL_EXPIRED_PERIOD_SECONDS;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailCertificationService {

    private final EmailCertificationRepository emailCertificationRepository;
    private static final long EXPIRED_TIME = EMAIL_EXPIRED_PERIOD_SECONDS;

    @Transactional(readOnly = true)
    public EmailCertification getEmailCertification(EmailCertKey emailCertKey) {
        Optional<EmailCertification> emailCert = emailCertificationRepository.findById(emailCertKey.getKey());
        return emailCert.isEmpty() ? null : emailCert.get();
    }

    @Transactional
    public void cert(EmailCert.CreateUpdate createUpdate) {
        EmailCertification emailCert = getEmailCertification(createUpdate);
        if (ObjectUtils.isEmpty(emailCert)) {
            throw new RuntimeException("발급된 토큰이 없습니다.");
        }
        createUpdate.checkSameToken(emailCert.getEmailToken());
        emailCert.setChecked(Boolean.TRUE);
        emailCertificationRepository.save(emailCert);
    }

    @Transactional
    public void register(EmailCert.CreateUpdate createUpdate) {
        EmailCertification of = EmailCertification.of(createUpdate.token(),
                createUpdate.githubId(),
                EXPIRED_TIME);
        emailCertificationRepository.save(of);
    }

    @Transactional
    public void delete(EmailCert.ReadDelete readDelete) {
        EmailCertification emailCertification = getEmailCertification(readDelete);
        emailCertificationRepository.delete(emailCertification);
    }
}
