package ru.practicum.shareit.booking.dto;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.dto.ItemDTO;
import ru.practicum.shareit.user.dto.UserDTO;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BookingForResponseTest {

    @Test
    void testEquals() {
        BookingForResponse booking1 = BookingForResponse.builder()
                .id(1)
                .startTime(LocalDateTime.of(2021, 1, 1, 10, 0))
                .endTime(LocalDateTime.of(2021, 1, 1, 12, 0))
                .build();

        BookingForResponse booking2 = BookingForResponse.builder()
                .id(1)
                .startTime(LocalDateTime.of(2021, 1, 1, 10, 0))
                .endTime(LocalDateTime.of(2021, 1, 1, 12, 0))
                .build();

        BookingForResponse booking3 = BookingForResponse.builder()
                .id(2)
                .startTime(LocalDateTime.of(2021, 1, 1, 10, 0))
                .endTime(LocalDateTime.of(2021, 1, 1, 12, 0))
                .build();

        assertEquals(booking1, booking2);
        assertNotEquals(booking1, booking3);
        assertEquals(booking1.hashCode(), booking2.hashCode());

    }

    @Test
    void testHashCode() {
        BookingForResponse booking1 = BookingForResponse.builder()
                .id(1)
                .startTime(LocalDateTime.of(2021, 1, 1, 10, 0))
                .endTime(LocalDateTime.of(2021, 1, 1, 12, 0))
                .build();

        BookingForResponse booking2 = BookingForResponse.builder()
                .id(1)
                .startTime(LocalDateTime.of(2021, 1, 1, 10, 0))
                .endTime(LocalDateTime.of(2021, 1, 1, 12, 0))
                .build();

        BookingForResponse booking3 = BookingForResponse.builder()
                .id(2)
                .startTime(LocalDateTime.of(2021, 1, 1, 10, 0))
                .endTime(LocalDateTime.of(2021, 1, 1, 12, 0))
                .build();

        assertEquals(booking1.hashCode(), booking2.hashCode()); // Ожидаем, что hashCode у booking1 равен hashCode у booking2
        assertNotEquals(booking1.hashCode(), booking3.hashCode()); // Ожидаем, что hashCode у booking1 не равен hashCode у booking3
    }

    @Test
    void bookingForResponseTest() {
        BookingForResponse bookingDto = BookingForResponse.builder()
                .id(1)
                .item(new ItemDTO())
                .startTime(LocalDateTime.now())
                .endTime(LocalDateTime.now().plusHours(2))
                .booker(new UserDTO())
                .status(Status.APPROVED)
                .build();

        assertNotNull(bookingDto);
        assertEquals(1, bookingDto.getId());

        assertNotNull(bookingDto.getStartTime());
        assertNotNull(bookingDto.getEndTime());
        assertNotNull(bookingDto.getItem());
        assertNotNull(bookingDto.getBooker());
        assertEquals(Status.APPROVED, bookingDto.getStatus());
    }
}