package ru.practicum.shareit.booking.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingForResponse;

import java.util.List;

@Service
@Transactional(readOnly = true)
public interface BookingService {
    @Transactional
    BookingForResponse makeBookingService(BookingDto bookingDto, int userId);

    BookingForResponse updateBooking(int bookingId, int ownerId, boolean approved);

    BookingForResponse getByBookerService(int bookingId, int ownerId);

    List<BookingForResponse> getAllForBookerService(String state, int userId);

    List<BookingForResponse> getAllForOwnerService(String state, int userId);

}
