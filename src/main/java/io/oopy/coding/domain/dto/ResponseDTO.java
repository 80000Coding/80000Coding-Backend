package io.oopy.coding.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseDTO<T> {
    private String status;
    private T data;
    private Map<String, Object> additionalData;

    public String getStatus() {
        return status;
    }

    public T getData() {
        return data;
    }

    public Map<String, Object> getAdditionalData() {
        return additionalData;
    }

    public ResponseDTO(String status, T data) {
        this.status = status;
        this.data = data;
        //this.additionalData = new HashMap<>();
    }

    public void addField(String fieldName, Object value) {
        additionalData.put(fieldName, value);
    }
}