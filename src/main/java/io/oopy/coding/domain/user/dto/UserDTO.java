package io.oopy.coding.domain.user.dto;

import io.oopy.coding.domain.organization.entity.UserOrganization;
import io.oopy.coding.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class UserDTO {
    private Long id;
    private String nickname;
    private List<String> userOrganizations;

    public static UserDTO toDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .nickname(user.getName())
                .userOrganizations(user.getOrganizationNames())
                .build();
    }
}
