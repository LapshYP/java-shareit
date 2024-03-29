package ru.practicum.shareit.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import java.util.Map;

@RestControllerAdvice
public class ExceptionController {

    //400
    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> validExceptionException(MethodArgumentNotValidException ex) {
        return Map.of("MethodArgumentNotValidException ", ex.getMessage());
    }

    //400+
    @ExceptionHandler({ConstraintViolationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> validExceptionException(ConstraintViolationException ex) {
        String erroMessage = ex.getMessage() != null ? ex.getMessage() : "the object has wrong fields";
        return Map.of("ConstraintViolationException ", erroMessage);
    }

    //400
    @ExceptionHandler({BadRequestException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> badRequestException(BadRequestException ex) {
        String erroMessage = ex.getMessage() != null ? ex.getMessage() : "the object has wrong fields";

        return Map.of("BadRequestException ", erroMessage);
    }


    //500
    @ExceptionHandler({MissingRequestHeaderException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handleException(MissingRequestHeaderException ex) {
        String erroMessage = ex.getMessage() != null ? ex.getMessage() : "the header don't exists";
        return Map.of("MissingRequestHeaderException ", erroMessage);
    }

    //500
    @ExceptionHandler({MissingPathVariableException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handleException(MissingPathVariableException ex) {
        String erroMessage = ex.getMessage() != null ? ex.getMessage() : "the pathVariable don't exists";
        return Map.of("MissingPathVariableException  ", erroMessage);
    }

    //  500+
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<?> handleException(final UnsupportedStatusException ex) {
        String msg = "{\"error\":\"Unknown state: UNSUPPORTED_STATUS\",\n" +
                "\"message\":\"UNSUPPORTED_STATUS\"}";
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(msg);
    }
}
