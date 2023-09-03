package io.oopy.coding.email.dto;

import io.oopy.coding.common.constant.EmailConstant;
import io.oopy.coding.email.service.dto.EmailCertificate;
import lombok.*;

import static io.oopy.coding.common.constant.EmailConstant.CERT_TITLE;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EmailRequest {

    @Getter
    public static class Send {

        private String targetEmail;

        public EmailCertificate.Send toCertificateEmailSend(String githubId) {
            return EmailCertificate.Send.of(this.targetEmail,
                    githubId,
                    CERT_TITLE,
                    null
                    );
        }
    }

    @Getter
    public static class Cert {
        private String githubId;
        private String token;
    }
}
