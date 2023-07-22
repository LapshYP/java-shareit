package ru.practicum.shareit.exception;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class UnsupportedStatusExceptionTest {
    @Test
    public void testUnsupportedStatusException() {
        String exceptionMsg = "Unsupported status exception";
        UnsupportedStatusException exception = new UnsupportedStatusException(exceptionMsg);
        assertEquals(exceptionMsg, exception.getMessage());
    }
}