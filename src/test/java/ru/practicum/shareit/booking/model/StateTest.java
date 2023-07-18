package ru.practicum.shareit.booking.model;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class StateTest {
    @Test
    public void testStateEnumValues() {

        State[] expectedValues = {State.ALL, State.CURRENT, State.PAST, State.FUTURE, State.WAITING, State.REJECTED, State.UNKNOWN};

        State[] actualValues = State.values();

        assertEquals(expectedValues.length, actualValues.length);
        for (int i = 0; i < expectedValues.length; i++) {
            assertEquals(expectedValues[i], actualValues[i]);
        }
    }
}