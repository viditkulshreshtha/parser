package com.log.parser.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.io.Serializable;


/**
 * ErrorDTO class is used to map the error response.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class ErrorDTO<T> implements Serializable {
    private T body;
    private String message;

    public ErrorDTO(HttpStatus httpStatus, String message) {
        this.body = (T) httpStatus;
        this.message = message;
    }
}
