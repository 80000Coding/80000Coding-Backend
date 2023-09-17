package io.oopy.coding.organization.service.dto;

import jakarta.persistence.Column;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.aspectj.weaver.ast.Or;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OrganizationDto {

    @Getter
    public static class Cert {
        private String organizationCode;
        private Long userId;
        private String userEmail;

        public Cert(String organizationCode, Long userId, String userEmail) {
            this.organizationCode = organizationCode;
            this.userId = userId;
            this.userEmail = userEmail;
        }

        public static Cert of(String organizationCode, Long userId, String userEmail) {
            return new Cert(organizationCode, userId, userEmail);
        }
    }

    @Getter
    public static class Organization {
        private String name;
        private String code;
        private String description;

        public Organization(String name, String code, String description) {
            this.name = name;
            this.code = code;
            this.description = description;
        }

        public static Organization of(String name, String code, String description) {
            return new Organization(name, code, description);
        }
    }

    @Getter
    public static class UserOrganization {
        private String name;
        private String code;
        private String description;
        private String email;

        public UserOrganization(String name, String code, String description, String email) {
            this.name = name;
            this.code = code;
            this.description = description;
            this.email = email;
        }

        public static UserOrganization of(String name, String code, String description, String email) {
            return new UserOrganization(name, code, description, email);
        }
    }
}
