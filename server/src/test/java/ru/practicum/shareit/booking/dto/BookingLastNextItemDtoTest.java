package ru.practicum.shareit.booking.dto;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.model.Status;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
class BookingLastNextItemDtoTest {
    @Mock
    BookingLastNextItemDto bookingLastNextItemDto;
    @Mock
    BookingLastNextItemDto bookingLastNextItemDto2;


    @Test
    void testEquals() {
        bookingLastNextItemDto = mock(BookingLastNextItemDto.class);
        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime endTime = LocalDateTime.now().plusHours(1);

        when(bookingLastNextItemDto.getId()).thenReturn(1);
        when(bookingLastNextItemDto.getStartTime()).thenReturn(startTime);
        when(bookingLastNextItemDto.getEndTime()).thenReturn(endTime);
        when(bookingLastNextItemDto.getBookerId()).thenReturn(2);
        when(bookingLastNextItemDto.getStatus()).thenReturn(Status.APPROVED);

        BookingLastNextItemDto otherBooking = new BookingLastNextItemDto(1, startTime, endTime, 2, Status.APPROVED);

        assertEquals(bookingLastNextItemDto.getId(), otherBooking.getId());
        assertEquals(bookingLastNextItemDto.getStatus(), otherBooking.getStatus());
        assertEquals(bookingLastNextItemDto.getBookerId(), otherBooking.getBookerId());
        assertEquals(bookingLastNextItemDto.getStartTime(), otherBooking.getStartTime());
        assertEquals(bookingLastNextItemDto.getEndTime(), otherBooking.getEndTime());
    }

//    @Test
//    void testEquals2() {
//        bookingLastNextItemDto = new BookingLastNextItemDto().builder()
//                .id(1)
//                .startTime(LocalDateTime.now().plusMonths(1))
//                .endTime(LocalDateTime.of(2024, 7, 9, 13, 56))
//                .bookerId(1)
//                .status(Status.WAITING)
//                .build();
//        bookingLastNextItemDto2 = new BookingLastNextItemDto().builder()
//                .id(1)
//                .startTime(LocalDateTime.now().plusMonths(1))
//                .endTime(LocalDateTime.of(2024, 7, 9, 13, 56))
//                .bookerId(1)
//                .status(Status.WAITING)
//                .build();

//        assertEquals(bookingLastNextItemDto, bookingLastNextItemDto2);
//        assertEquals(bookingLastNextItemDto.hashCode(), bookingLastNextItemDto2.hashCode());
//    }
}