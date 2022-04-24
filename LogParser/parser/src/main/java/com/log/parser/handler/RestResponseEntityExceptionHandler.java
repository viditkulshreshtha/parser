package com.log.parser.handler;


import com.log.parser.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Exception handler for rest controller
 */

@RestControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
    /**
     * Method to handle all the exception and return the error response
     *
     * @param ex: Exception
     * @return : ErrorResponse
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse<?> handleAllException(Exception ex, WebRequest request) {
        return new ErrorResponse<>(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    }

    /**
     * Method to handle IllegalArgumentException and return the error response
     *
     * @param ex: Exception
     * @return : ErrorResponse
     */
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse<?> handleIllegalArgumentException(Exception ex, WebRequest request) {
        return new ErrorResponse<>(HttpStatus.BAD_REQUEST, ex.getMessage());
    }
}