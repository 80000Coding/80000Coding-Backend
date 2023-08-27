package io.oopy.coding.domain.user.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;

@RequiredArgsConstructor
public enum RoleType {
    ADMIN("ROLE_ADMIN"),
    USER("ROLE_USER");

    private final String role;
    private static final Map<String, RoleType> stringToEnum =
            Stream.of(values()).collect(toMap(Object::toString, e -> e));

    @JsonValue
    public String getRole() { return role; }

    @JsonCreator
    public static RoleType fromString(String role) {
        return stringToEnum.get(role.toUpperCase());
    }

    @Override public String toString() { return role; }
}