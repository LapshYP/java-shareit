package ru.practicum.shareit.booking.service;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingForResponse;

import java.util.List;

@Service
public interface BookingService {

    BookingForResponse makeBookingService(BookingDto bookingDto, int userId);

    BookingForResponse updateBooking(int bookingId, int ownerId, boolean approved);

    BookingForResponse getByBookerService(int bookingId, int ownerId);

    List<BookingForResponse> getAllForBookerService(String state, int userId, int from, int size);

    List<BookingForResponse> getAllForOwnerService(String state, int userId, int from, int size);

}
