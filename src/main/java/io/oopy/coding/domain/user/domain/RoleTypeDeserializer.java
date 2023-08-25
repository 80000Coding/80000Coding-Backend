package io.oopy.coding.domain.user.domain;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class RoleTypeDeserializer extends JsonDeserializer<RoleType> {

    @Override
    public RoleType deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
        String role = p.getValueAsString();
        log.info("role: {}", role);
        if (!role.isEmpty()) {
            if (role.equalsIgnoreCase(RoleType.ADMIN.getRole()))
                return RoleType.ADMIN;
            if (role.equalsIgnoreCase(RoleType.USER.getRole()))
                return RoleType.USER;
        }
        throw new IllegalArgumentException("RoleType is not valid: " + role);
    }
}
