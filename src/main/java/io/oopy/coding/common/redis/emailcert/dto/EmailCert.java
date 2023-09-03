package io.oopy.coding.common.redis.emailcert.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EmailCert {

    public record ReadDelete(String githubId) implements EmailCertKey {
        public static EmailCert.ReadDelete of(
                @NotBlank(message = "githubId must not be empty") String githubId) {
            return new ReadDelete(githubId);
        }

        @Override
        public String getKey() {
            return githubId;
        }
    }

    public record CreateUpdate(String githubId, String token) implements EmailCertKey {
        @Override
        public String getKey() {
            return githubId;
        }

        public void checkSameToken(String token) {
            if(!this.token.equals(token)) {
                throw new RuntimeException("token이 일치하지 않습니다.");
            }
        }

        public static EmailCert.CreateUpdate of(
                @NotBlank(message = "githubId must not be empty")String githubId,
                @NotBlank(message = "token must not be empty") String token) {
            return new CreateUpdate(githubId, token);
        }
    }
}
