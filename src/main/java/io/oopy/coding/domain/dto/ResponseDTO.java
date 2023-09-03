package io.oopy.coding.domain.dto;

import java.util.HashMap;
import java.util.Map;

public class ResponseDTO<T> {
    private String status;
    private T data;
    private Map<String, Object> additionalData;

    public ResponseDTO(String status, T data) {
        this.status = status;
        this.data = data;
        this.additionalData = new HashMap<>();
    }

    public void addField(String fieldName, Object value) {
        additionalData.put(fieldName, value);
    }
}