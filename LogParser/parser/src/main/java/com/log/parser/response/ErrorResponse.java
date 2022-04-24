package com.log.parser.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.log.parser.dto.ErrorDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;


/**
 * Error response class for wrapping error details
 */

@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse<T> {
    private ErrorDTO<T> error;

    public ErrorResponse(HttpStatus httpStatus, String message) {
        this.error = new ErrorDTO<>(httpStatus, message);
    }
}
