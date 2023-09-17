package io.oopy.coding.organization.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OrganizationResponse {

    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class UserOrganization {
        private String code;
        private String organizationName;
        private String userEmail;

        public static UserOrganization of(String code, String organizationName, String userEmail) {
            return new UserOrganization(code, organizationName, userEmail);
        }
    }
}
