package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingForResponse;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repossitory.BookingRepoJpa;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.UnsupportedStatusException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepoJpa;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepoJpa;

import javax.validation.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepoJpa bookingRepoJpa;
    private final ItemRepoJpa itemRepoJpa;
    private final UserRepoJpa userRepoJpa;

    private final ModelMapper mapper = new ModelMapper();

    private void validateBooking(Booking booking) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<Booking>> violations = validator.validate(booking);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }

    @Override
    @Transactional
    public BookingForResponse makeBookingService(BookingDto bookingDto, int userId) {
        Item item = itemRepoJpa.findById(bookingDto.getItemId()).orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND, "Вещь не найдена"));

        if (itemRepoJpa.findById(bookingDto.getItemId()).get().getOwner().getId() == userId) {
            throw new NotFoundException(HttpStatus.NOT_FOUND, "Вещь не может быть заказана собственником этой вещи");

        }
        if (bookingDto.getStart() == null ||
                bookingDto.getEnd() == null) {
            throw new BadRequestException(HttpStatus.NOT_FOUND, "Ошибка времени создания букинга");
        }
        if (bookingDto.getStart().isAfter(bookingDto.getEnd()) ||
                bookingDto.getStart().isEqual(bookingDto.getEnd())
        ) {
            throw new BadRequestException(HttpStatus.NOT_FOUND, "Ошибка времени создания букинга");
        }

        if (bookingDto.getStart().isBefore(LocalDateTime.now())
        ) {
            throw new BadRequestException(HttpStatus.NOT_FOUND, "Ошибка времени создания букинга");
        }
        if (item.getAvailable() == false) {
            throw new BadRequestException(HttpStatus.NOT_FOUND, "вещь не доступна");
        }
        User booker = userRepoJpa.findById(userId).orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND, "Юзер не найден"));

        Booking booking = mapper.map(bookingDto, Booking.class);
        booking.setBooker(booker);
        booking.setItem(item);
        booking.setStatus(Status.WAITING);
        validateBooking(booking);
        Booking bookingToSave = bookingRepoJpa.save(booking);
        BookingForResponse bookingForResponse = mapper.map(bookingToSave, BookingForResponse.class);
        return bookingForResponse;
    }

    @Override
    @Transactional
    public BookingForResponse updateBooking(int bookingId, int userId, boolean approved) {
        Optional<Booking> bookingRepoJpaById = bookingRepoJpa.findById(bookingId);
        Booking booking = bookingRepoJpaById.orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND, "Букинг с таким айди не найден"));
        int itemOwnerId = booking.getItem().getOwner().getId();
        if (itemOwnerId != userId) {
            throw new NotFoundException(HttpStatus.FOUND,
                    "User with id " + userId + " is not owner for item from booking with id " + bookingId);
        }

        if (approved && !booking.getStatus().equals(Status.APPROVED)) {
            booking.setStatus(Status.APPROVED);
            validateBooking(booking);
            bookingRepoJpa.save(booking);
        } else if (!approved && !booking.getStatus().equals(Status.REJECTED)) {
            booking.setStatus(Status.REJECTED);
            validateBooking(booking);
            bookingRepoJpa.save(booking);
        } else {
            throw new BadRequestException(HttpStatus.BAD_REQUEST, "Status is already updated for booking with id " + bookingId);
        }

        return mapper.map(booking, BookingForResponse.class);
    }

    @Override
    @Transactional(readOnly = true)
    public BookingForResponse getByBookerService(int bookingId, int bookerId) {
        Booking booking = bookingRepoJpa.findById(bookingId).orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND, "Booking with id " + bookingId + " not exists in the DB"));

        int itemOwnerId = booking.getItem().getOwner().getId();
        int whoBooked = booking.getBooker().getId();
        if (bookerId == itemOwnerId || bookerId == whoBooked) {
            return mapper.map(booking, BookingForResponse.class);
        } else throw new NotFoundException(HttpStatus.NOT_FOUND, "Заказчик  не совпадает с собственником вещи");

    }

    @SneakyThrows
    @Override
    @Transactional(readOnly = true)
    public List<BookingForResponse> getAllForBookerService(String bookingState, int userId, int from, int size) {
        User booker
                = userRepoJpa.findById(userId)
                .orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND, "User with id " + userId + " not exists in the DB"));

        if (from < 0 || size < 0 || (from == 0 && size == 0)) {
            throw new BadRequestException(HttpStatus.BAD_REQUEST, "Ошибка в параметрах Pagination");
        }
        Pageable paging = PageRequest.of(from / size, size, Sort.by("start"));

        State state;
        if (bookingState == null) {
            state = State.ALL;
        } else {
            try {
                state = State.valueOf(bookingState);
            } catch (IllegalArgumentException e) {
                state = State.UNKNOWN;
            }

        }

        List<Booking> bookingList = new ArrayList<>();
        switch (state) {
            case ALL:
                bookingList = bookingRepoJpa.findAllByBookerOrderByStartDesc(booker, paging);
                break;
            case FUTURE:
                bookingList = bookingRepoJpa.findAllByBookerAndStartIsAfterOrderByStartDesc(
                        booker, LocalDateTime.now());
                break;
            case WAITING:
                bookingList = bookingRepoJpa.findAllByBookerAndStatusEqualsOrderByStartDesc(
                        booker, Status.WAITING);
                break;
            case REJECTED:
                bookingList = bookingRepoJpa.findAllByBookerAndStatusEqualsOrderByStartDesc(
                        booker, Status.REJECTED);
                break;
            case CURRENT:
                bookingList = bookingRepoJpa.findAllBookingsForBookerWithStartAndEnd(
                        booker, LocalDateTime.now(), LocalDateTime.now());
                break;
            case PAST:
                bookingList = bookingRepoJpa.findAllByBookerAndEndIsBeforeOrderByStartDesc(
                        booker, LocalDateTime.now());
                break;
            case UNKNOWN:
                throw new UnsupportedStatusException("Unknown bookingState: UNSUPPORTED_STATUS");
        }

        List<BookingForResponse> bookingsListForResponse = new ArrayList<>();

        for (Booking booking : bookingList) {
            BookingForResponse bookingForResponse = mapper.map(booking, BookingForResponse.class);
            bookingsListForResponse.add(bookingForResponse);
        }

        return bookingsListForResponse;
    }


    @Override
    @Transactional(readOnly = true)
    public List<BookingForResponse> getAllForOwnerService(String bookingState, int userId, int from, int size) {
        User owner
                = userRepoJpa.findById(userId)
                .orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND, "User with id " + userId + " not exists in the DB"));

        if (from < 0 || size < 0 || (from == 0 && size == 0)) {
            throw new BadRequestException(HttpStatus.BAD_REQUEST, "Ошибка в параметрах Pagination");
        }
        Pageable paging = PageRequest.of(from / size, size, Sort.by("start"));

        List<Booking> bookingListResult = new ArrayList<>();
        State state;
        if (bookingState == null) {
            state = State.ALL;
        } else {
            try {
                state = State.valueOf(bookingState);
            } catch (IllegalArgumentException e) {
                state = State.UNKNOWN;
            }
        }

        switch (state) {
            case ALL:
                bookingListResult = bookingRepoJpa.getAllForOwner(owner.getId(), paging);
                break;
            case FUTURE:
                bookingListResult = bookingRepoJpa.findAllByOwnerAndStartIsAfterOrderByStartDesc(
                        owner.getId(), LocalDateTime.now());
                break;
            case WAITING:
                bookingListResult = bookingRepoJpa.findAllByOwnerAndStatusEqualsOrderByStartDesc(
                        owner.getId(), Status.WAITING);
                break;
            case REJECTED:
                bookingListResult = bookingRepoJpa.findAllByOwnerAndStatusEqualsOrderByStartDesc(
                        owner.getId(), Status.REJECTED);
                break;
            case CURRENT:
                bookingListResult = bookingRepoJpa.findAllBookingsForOwnerWithStartAndEnd(
                        owner, LocalDateTime.now(), LocalDateTime.now());
                break;
            case PAST:
                bookingListResult = bookingRepoJpa.findAllByOwnerAndEndIsBeforeOrderByStartDesc(
                        owner, LocalDateTime.now());
                break;
            case UNKNOWN:
                throw new UnsupportedStatusException("Unknown bookingState: UNSUPPORTED_STATUS");
        }
        List<BookingForResponse> bookingsForResponse = new ArrayList<>();

        for (Booking booking : bookingListResult) {
            BookingForResponse bookingForResponse = mapper.map(booking, BookingForResponse.class);
            bookingsForResponse.add(bookingForResponse);
        }
        return bookingsForResponse;
    }
}

