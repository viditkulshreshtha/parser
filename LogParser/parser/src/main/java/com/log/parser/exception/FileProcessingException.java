package com.log.parser.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.IOException;


/**
 * Custom Exception class for File Processing
 */

@Data
@AllArgsConstructor
@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class FileProcessingException extends IOException {
    private String message;
    private HttpStatus httpStatus;
}
