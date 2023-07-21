package ru.practicum.shareit.exception;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class NotFoundExceptionTest {
    @Test
    public void testConstructor() {
        String expectedMsg = "Not found";
        NotFoundException exception = new NotFoundException(HttpStatus.NOT_FOUND, expectedMsg);
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());

    }

    @Test
    public void testConstructor_withCustomStatusAndMessage() {
        String expectedMsg = "Custom message";
        HttpStatus expectedStatus = HttpStatus.BAD_REQUEST;
        NotFoundException exception = new NotFoundException(expectedStatus, expectedMsg);
        assertEquals(expectedStatus, exception.getStatus());

    }


}