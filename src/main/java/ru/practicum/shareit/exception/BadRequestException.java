package ru.practicum.shareit.exception;

public class BadRequestException extends Throwable {
    public BadRequestException(String msg) {
        super (msg);
    }
}
