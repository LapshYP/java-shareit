package ru.practicum.shareit.exception;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


import java.util.Map;

@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler({NotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> notFoundException(NotFoundException ex) {
        String erroMessage = ex.getMessage() != null ? ex.getMessage() : "the object does not exists";

        return Map.of("NotFoundException ", erroMessage);
    }

    @ExceptionHandler({DubleException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> dubleException(DubleException ex) {
        String erroMessage = ex.getMessage() != null ? ex.getMessage() : "the object already exists";
        return Map.of("DubleException ", erroMessage);
    }

    @ExceptionHandler({MissingRequestHeaderException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handleException(MissingRequestHeaderException ex) {
        String erroMessage = ex.getMessage() != null ? ex.getMessage() : "the header don't exists";
        return Map.of("MissingRequestHeaderException ", erroMessage);
    }
}
