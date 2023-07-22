package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingForResponse;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;


    @PostMapping
    private BookingForResponse makeBooking(@RequestBody(required = false) BookingDto bookingDto, @RequestHeader("X-Sharer-User-Id") int userId) {
        return bookingService.makeBookingService(bookingDto, userId);
    }

    @PatchMapping("/{bookingId}")
    private BookingForResponse setApproveByOwnerCurrent(@PathVariable int bookingId, @RequestHeader("X-Sharer-User-Id") int ownerId, @RequestParam boolean approved) {

        return bookingService.updateBooking(bookingId, ownerId, approved);
    }

    @GetMapping("/{bookingId}")
    private BookingForResponse getByBooker(@PathVariable int bookingId, @RequestHeader("X-Sharer-User-Id") int bookerId) {
        return bookingService.getByBookerService(bookingId, bookerId);
    }

    @GetMapping
    private List<BookingForResponse> getAllForUser(@RequestParam(required = false, defaultValue = "ALL") String state,
                                                   @RequestHeader("X-Sharer-User-Id") int userId,
                                                   @RequestParam(name = "from", defaultValue = "0") int from,
                                                   @RequestParam(name = "size", defaultValue = "100") int size)  {
        return bookingService.getAllForBookerService(state, userId, from, size);
    }

    @GetMapping("/owner")
    private List<BookingForResponse> getAllForOwner(@RequestParam(required = false, defaultValue = "ALL") String state,
                                                    @RequestHeader("X-Sharer-User-Id") int userId,
                                                    @RequestParam(name = "from", defaultValue = "0") int from,
                                                    @RequestParam(name = "size", defaultValue = "100") int size)  {
        return bookingService.getAllForOwnerService(state, userId, from, size);
    }
}
