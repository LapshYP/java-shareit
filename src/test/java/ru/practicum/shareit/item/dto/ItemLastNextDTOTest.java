package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.dto.BookingLastNextItemDto;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class ItemLastNextDTOTest {

    @Test
    void testEquals() {
        ItemLastNextDTO item1 = ItemLastNextDTO.builder()
                .id(1)
                .name("item")
                .description("description")
                .available(true)
                .ownerId(2)
                .request(3)
                .lastBooking(new BookingLastNextItemDto())
                .nextBooking(new BookingLastNextItemDto())
                .comments(new ArrayList<>())
                .build();

        ItemLastNextDTO item2 = ItemLastNextDTO.builder()
                .id(1)
                .name("item")
                .description("description")
                .available(true)
                .ownerId(2)
                .request(3)
                .lastBooking(new BookingLastNextItemDto())
                .nextBooking(new BookingLastNextItemDto())
                .comments(new ArrayList<>())
                .build();

        assertEquals(item1, item2);
    }

    @Test
    void testHashCode() {
        ItemLastNextDTO item1 = ItemLastNextDTO.builder()
                .id(1)
                .name("item")
                .description("description")
                .available(true)
                .ownerId(2)
                .request(3)
                .lastBooking(new BookingLastNextItemDto())
                .nextBooking(new BookingLastNextItemDto())
                .comments(new ArrayList<>())
                .build();

        ItemLastNextDTO item2 = ItemLastNextDTO.builder()
                .id(1)
                .name("item")
                .description("description")
                .available(true)
                .ownerId(2)
                .request(3)
                .lastBooking(new BookingLastNextItemDto())
                .nextBooking(new BookingLastNextItemDto())
                .comments(new ArrayList<>())
                .build();

        assertEquals(item1.hashCode(), item2.hashCode());
    }
}