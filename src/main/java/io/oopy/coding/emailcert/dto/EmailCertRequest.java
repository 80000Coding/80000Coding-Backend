package io.oopy.coding.emailcert.dto;

import io.oopy.coding.emailcert.service.dto.EmailCertificateDto;
import lombok.*;

import static io.oopy.coding.common.constant.EmailConstant.CERT_TITLE;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EmailCertRequest {

    @Getter
    public static class Send {

        private String targetEmail;
        private String organizationCode;

        public EmailCertificateDto.Send toCertificateEmailSend(String githubId) {
            return EmailCertificateDto.Send.of(this.targetEmail,
                    githubId,
                    CERT_TITLE,
                    null,
                    this.organizationCode
                    );
        }
    }

    @Getter
    public static class Cert {
        private String token;
        private String code;
    }

    @Getter
    public static class Remove {
        private String code;
    }
}
