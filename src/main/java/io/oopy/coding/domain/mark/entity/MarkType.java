package io.oopy.coding.domain.mark.entity;

import com.fasterxml.jackson.annotation.JsonValue;
import io.oopy.coding.common.util.converter.LegacyCommonType;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum MarkType implements LegacyCommonType {
    //TODO 코드가 필요한가, 필요하다면 어떻게 작성할 것인가??
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

    public static MarkType fromString(String type) {
        for (MarkType markType : values()) {
            if (markType.getType().equals(type)) {
                return markType;
            }
        }
        return null;
    }
}
