package io.oopy.coding.emailcert.service.dto;

import io.oopy.coding.common.email.dto.EmailSend;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EmailCertificateDto {

    @Getter
    public static class Send extends EmailSend {

        private String userId;
        private String organizationCode;

        public Send(String targetEmail, String userId, String title, String content, String organizationCode) {
            super(targetEmail, title, content);
            this.userId = userId;
            this.organizationCode = organizationCode;
        }

        public static Send of(String targetEmail, String githubId, String title, String content, String organizationCode) {
            return new Send(targetEmail,
                    githubId,
                    title,
                    content,
                    organizationCode
            );
        }
    }
}
