package com.log.parser.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;


/**
 * SuccessDTO class to hold the success response
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class SuccessDTO<T> implements Serializable {
    private T body;
    private String message = null;

    public SuccessDTO(T object) {
        this.body = object;
    }
}
