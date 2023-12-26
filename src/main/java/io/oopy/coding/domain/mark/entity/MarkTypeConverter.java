package io.oopy.coding.domain.mark.entity;

import io.oopy.coding.common.util.converter.AbstractLegacyEnumAttributeConverter;
import jakarta.persistence.Converter;
import lombok.extern.slf4j.Slf4j;

@Converter
@Slf4j
public class MarkTypeConverter extends AbstractLegacyEnumAttributeConverter<MarkType> {
    private static final String ENUM_NAME = "마크 타입";

    public MarkTypeConverter() {
        super(MarkType.class, false, ENUM_NAME);
    }

}
