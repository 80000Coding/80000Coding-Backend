package io.oopy.coding.email.service.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EmailCertificate {

    @Getter
    public static class Send extends EmailSend{

        private String githubId;

        public Send(String targetEmail, String githubId, String title, String content) {
            super(targetEmail, title, content);
            this.githubId = githubId;
        }

        public static Send of(String targetEmail, String githubId, String title, String content) {
            return new Send(targetEmail,
                    githubId,
                    title,
                    content
            );
        }
    }
}
