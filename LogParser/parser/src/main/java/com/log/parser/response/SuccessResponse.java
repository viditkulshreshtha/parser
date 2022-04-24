package com.log.parser.response;

import com.log.parser.dto.SuccessDTO;
import lombok.Data;

/**
 * SuccessResponse class for wrapping success response
 */
@Data
public class SuccessResponse<T> {
    private SuccessDTO<T> response;

    public SuccessResponse(T object) {
        this.response = new SuccessDTO<>(object);
    }
}