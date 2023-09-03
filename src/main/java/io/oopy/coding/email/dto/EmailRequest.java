package io.oopy.coding.email.dto;

import io.oopy.coding.common.constant.EmailConstant;
import io.oopy.coding.email.service.dto.EmailSend;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EmailRequest {

    @Getter
    public static class Send {
        private String targetEmail;

        public EmailSend toEmailSend(String githubId) {
            return EmailSend.of(this.targetEmail,
                    githubId,
                    EmailConstant.TITLE,
                    EmailConstant.CONTENT
                    );
        }
    }
}
