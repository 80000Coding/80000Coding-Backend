package io.oopy.coding.common.redis.organizationcert.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OrganizationCertificationDto {

    private static final String KEY_PREFIX = "org-cert:";

    public record Register(String userId, String token, String code,
                           String organizationName, String userEmail) implements OrganizationCertKey {

        @Override
        public String getKey() {
            return KEY_PREFIX + userId;
        }

        @Override
        public String getOrganizationCode() {
            return code;
        }

        public void checkSameToken(String token) {
            if(!this.token.equals(token)) {
                throw new RuntimeException("token이 일치하지 않습니다.");
            }
        }

        public static OrganizationCertificationDto.Register of(
                @NotBlank(message = "userId must not be empty")String userId,
                @NotBlank(message = "token must not be empty") String token,
                @NotBlank(message = "code must not be empty") String code,
                @NotBlank(message = "organizationName must not be empty")  String organizationName,
                @NotBlank(message = "email must not be empty") String email
        ) {
            return new OrganizationCertificationDto.Register(userId, token, code, organizationName, email);
        }
    }

    public record Cert(String userId, String token, String code) implements OrganizationCertKey {

        @Override
        public String getKey() {
            return KEY_PREFIX + userId;
        }

        @Override
        public String getOrganizationCode() {
            return code;
        }

        public void checkSameToken(String token) {
            if(!this.token.equals(token)) {
                throw new RuntimeException("token이 일치하지 않습니다.");
            }
        }

        public static OrganizationCertificationDto.Cert of(
                @NotBlank(message = "userId must not be empty")String userId,
                @NotBlank(message = "token must not be empty") String token,
                @NotBlank(message = "code must not be empty") String code
        ) {
            return new OrganizationCertificationDto.Cert(userId, token, code);
        }
    }

    public record Get(String userId, String code) implements OrganizationCertKey {

        public static OrganizationCertificationDto.Get of(
                @NotBlank(message = "userId must not be empty") String userId,
                @NotBlank(message = "code must not be empty") String code
        ) {
            return new OrganizationCertificationDto.Get(userId, code);
        }

        @Override
        public String getKey() {
            return KEY_PREFIX + userId;
        }

        @Override
        public String getOrganizationCode() {
            return code;
        }
    }

    public record Delete(String userId, String code) implements OrganizationCertKey {

        public static OrganizationCertificationDto.Delete of(
                @NotBlank(message = "userId must not be empty") String userId,
                @NotBlank(message = "code must not be empty") String code
        ) {
            return new OrganizationCertificationDto.Delete(userId, code);
        }

        @Override
        public String getKey() {
            return KEY_PREFIX + userId;
        }

        @Override
        public String getOrganizationCode() {
            return code;
        }
    }

    public record Key(String userId) implements OrganizationKey {
        public static OrganizationCertificationDto.Key of(
                @NotBlank(message = "userId must not be empty") String userId
        ) {
            return new OrganizationCertificationDto.Key(userId);
        }

        @Override
        public String getKey() {
            return KEY_PREFIX + userId;
        }
    }
}
