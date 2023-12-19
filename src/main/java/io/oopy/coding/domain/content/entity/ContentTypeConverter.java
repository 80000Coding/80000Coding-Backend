package io.oopy.coding.domain.content.entity;

import io.oopy.coding.common.util.converter.AbstractLegacyEnumAttributeConverter;
import jakarta.persistence.Converter;
import lombok.extern.slf4j.Slf4j;

@Converter
@Slf4j
public class ContentTypeConverter extends AbstractLegacyEnumAttributeConverter<ContentType> {
    private static final String ENUM_NAME = "게시글 타입";

    public ContentTypeConverter() {
        super(ContentType.class, false, ENUM_NAME);
    }
}
