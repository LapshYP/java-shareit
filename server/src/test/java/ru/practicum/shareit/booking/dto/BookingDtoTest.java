package ru.practicum.shareit.booking.dto;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class BookingDtoTest {
    @Test
    public void testCreateBookingDto() {
        BookingDto bookingDto = BookingDto.builder()
                .id(1)
                .itemId(123)
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusHours(2))
                .item(new Item())
                .booker(new User())
                .status(Status.APPROVED)
                .build();

        assertNotNull(bookingDto);
        assertEquals(1, bookingDto.getId());
        assertEquals(123, bookingDto.getItemId());
        assertNotNull(bookingDto.getStart());
        assertNotNull(bookingDto.getEnd());
        assertNotNull(bookingDto.getItem());
        assertNotNull(bookingDto.getBooker());
        assertEquals(Status.APPROVED, bookingDto.getStatus());
    }

}