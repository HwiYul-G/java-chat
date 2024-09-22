package com.y.javachat.system.exception;

import lombok.Getter;

@Getter
public class ExternalAPIException extends RuntimeException {

    private final Integer status;
    private final String responseBody;

    public ExternalAPIException(String message, Integer status, String responseBody) {
        super(message);
        this.status = status;
        this.responseBody = responseBody;
    }

}
