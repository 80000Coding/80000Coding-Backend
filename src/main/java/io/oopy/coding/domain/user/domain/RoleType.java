package io.oopy.coding.domain.user.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;

@Getter
@RequiredArgsConstructor
public enum RoleType {
    ADMIN("ROLE_ADMIN"),
    USER("ROLE_USER");

    private final String role;

    @Override public String toString() { return role; }
    public static RoleType fromString(String role) {
        return stringToEnum.get(role);
    }

    private static final Map<String, RoleType> stringToEnum =
            Stream.of(values()).collect(toMap(Object::toString, e -> e));

}