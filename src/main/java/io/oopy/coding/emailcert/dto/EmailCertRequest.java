package io.oopy.coding.emailcert.dto;

import io.oopy.coding.emailcert.service.dto.EmailCertificateDto;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import static io.oopy.coding.common.constant.EmailConstant.CERT_TITLE;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EmailCertRequest {

    @Getter
    public static class Send {
        @NotBlank(message = "이메일은 필수 입력 값입니다.")
        private String targetEmail;
        @NotBlank(message = "조직 코드는 필수로 존재해야 합니다.")
        private String organizationCode;

        public EmailCertificateDto.Send toCertificateEmailSend(String userId) {
            return EmailCertificateDto.Send.of(this.targetEmail,
                    userId,
                    CERT_TITLE,
                    null,
                    this.organizationCode
                    );
        }
    }

    @Getter
    public static class Remove {
        private String code;
    }
}
