package io.oopy.coding.api.mark;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum MarkType {
    LIKE("1", "like"),
    BOOKMARK("2", "bookmark");

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

    public static MarkType fromType(String type) {
        for (MarkType markType : values()) {
            if (markType.getType().equals(type)) {
                return markType;
            }
        }
        return null;
    }
}
