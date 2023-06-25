package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingForResponse;
import ru.practicum.shareit.booking.repossitory.BookingRepoJpa;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.repository.ItemRepoJpa;
import ru.practicum.shareit.user.repository.UserRepoJpa;

import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;
    private final BookingRepoJpa bookingRepoJpa;
    private final ItemRepoJpa itemRepoJpa;
    private final UserRepoJpa userRepoJpa;

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
                                                   @RequestHeader("X-Sharer-User-Id") int userId) {
        return bookingService.getAllForUserService(state, userId);
    }
    @GetMapping("/owner")
    private List<BookingForResponse> getAllForOwner(@RequestParam(required = false, defaultValue = "ALL") String state,
                                                          @RequestHeader("X-Sharer-User-Id") int userId) {
        return bookingService.getAllForOwnerService(state, userId);
    }
}
