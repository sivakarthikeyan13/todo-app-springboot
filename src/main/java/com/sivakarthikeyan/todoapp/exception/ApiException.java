package com.sivakarthikeyan.todoapp.exception;

import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

public class ApiException {
    private final String errorMessage;
    private final HttpStatus httpStatus;
    private final ZonedDateTime timeStamp;

    public ApiException(String message, HttpStatus httpStatus, ZonedDateTime timeStamp) {
        this.errorMessage = message;
        this.httpStatus = httpStatus;
        this.timeStamp = timeStamp;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public ZonedDateTime getTimeStamp() {
        return timeStamp;
    }
}
