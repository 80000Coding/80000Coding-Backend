package io.oopy.coding.api.content;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ContentType {
    // TODO 코드가 필요한가, 필요하다면 어떻게 작성할 것인가??
    POST("1", "post"),
    PROJECT("2", "project");

    private final String code;
    private final String type;

    @JsonValue
    public String getCode() {
        return code;
    }

    @JsonValue
    public String getType() {
        return type;
    }

    public static ContentType fromString(String type) {
        for (ContentType contentType : values()) {
            if (contentType.getType().equals(type)) {
                return contentType;
            }
        }
        return null;
    }
}
