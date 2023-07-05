package ru.practicum.shareit.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

// sets the HTTP status code to 400
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException extends ResponseStatusException {
    public BadRequestException(HttpStatus httpStatus, String message) {
        super(httpStatus, message);
    }

}