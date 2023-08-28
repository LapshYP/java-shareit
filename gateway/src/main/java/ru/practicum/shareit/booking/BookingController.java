package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.UnsupportedStatusException;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {

    private final BookingClient bookingClient;

    @GetMapping
    public ResponseEntity<Object> getAllForUser(@RequestHeader("X-Sharer-User-Id") int userId,
                                                @RequestParam(name = "state", defaultValue = "ALL") String stateParam,
                                                @PositiveOrZero
                                                @RequestParam(name = "from", defaultValue = "0") int from,
                                                @Positive
                                                @RequestParam(name = "size", defaultValue = "20") int size) {
        BookingState state = BookingState.from(stateParam)
                .orElseThrow(() -> new UnsupportedStatusException("Unknown bookingState: UNSUPPORTED_STATUS"));
        return bookingClient.getAllForBookerService(state, userId, from, size);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getAllForOwner(@RequestHeader("X-Sharer-User-Id") int userId,
                                                 @RequestParam(name = "state", defaultValue = "ALL") String stateParam,
                                                 @PositiveOrZero
                                                 @RequestParam(defaultValue = "0") int from,
                                                 @Positive

                                                 @RequestParam(defaultValue = "20") int size) {
        BookingState state = BookingState.from(stateParam)
                .orElseThrow(() -> new UnsupportedStatusException("Unknown bookingState: UNSUPPORTED_STATUS"));
        return bookingClient.getAllForOwnerService(state, userId, from, size);
    }

    @PostMapping
    public ResponseEntity<Object> makeBooking(@RequestHeader("X-Sharer-User-Id") int userId,
                                              @RequestBody @Valid BookingDto bookingDto) {

        return bookingClient.makeBookingService(bookingDto, userId);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> setApproveByOwnerCurrent(@RequestHeader("X-Sharer-User-Id") int userId,
                                                           @PathVariable("bookingId") int bookingId,
                                                           @RequestParam boolean approved) {
        return bookingClient.updateBooking(bookingId, userId, approved);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getByBooker(@RequestHeader("X-Sharer-User-Id") int userId,
                                              @PathVariable int bookingId) {
        return bookingClient.getByBookerService(bookingId, userId);
    }
}