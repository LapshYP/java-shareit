package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
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
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepoJpa;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepoJpa bookingRepoJpa;
    private final ItemRepoJpa itemRepoJpa;
    private final UserRepoJpa userRepoJpa;
    // private final BookingMapper bookingMapper;
    private final UserService userService;
    private final ItemService itemService;
    //    private final BookingMapper bookingMapper
//            = Mappers.getMapper(BookingMapper.class);
    private final ModelMapper mapper = new ModelMapper();

    @Override
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

        //  Booking booking = bookingMapper.bookingDtoToBooking(bookingDto);
        Booking booking = mapper.map(bookingDto, Booking.class);
        booking.setBooker(booker);
        booking.setItem(item);
        booking.setStatus(Status.WAITING);

        Booking bookingToSave = bookingRepoJpa.save(booking);
        // BookingForResponse bookingForResponse = bookingMapper.bookingToResponseBookingDto(bookingToSave);
        BookingForResponse bookingForResponse = mapper.map(bookingToSave, BookingForResponse.class);
        return bookingForResponse;
    }

    @Override
    public BookingForResponse updateBooking(int bookingId, int userId, boolean approved) {
        Optional<Booking> bookingRepoJpaById = bookingRepoJpa.findById(bookingId);
        Booking booking = bookingRepoJpaById.orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND, "Букинг с таким айди не найден"));
        int itemOwnerId = booking.getItem().getOwner().getId();
        if (itemOwnerId != userId) {
            throw new NotFoundException(HttpStatus.FOUND,
                    "User with id " + userId + " is not owner for item from booking with id " + bookingId);
        }

        //       itemService.getByUserIdService(itemOwnerId);
        if (approved && !booking.getStatus().equals(Status.APPROVED)) {
            booking.setStatus(Status.APPROVED);
            bookingRepoJpa.save(booking);
        } else if (!approved && !booking.getStatus().equals(Status.REJECTED)) {
            booking.setStatus(Status.REJECTED);
            bookingRepoJpa.save(booking);
        } else {
            throw new BadRequestException(HttpStatus.BAD_REQUEST, "Status is already updated for booking with id " + bookingId);
        }

//        return bookingMapper.bookingToResponseBookingDto(bookingRepoJpa.findById(bookingId)
//                .orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND, "Booking with id " + bookingId + " not exists in the DB")));

        return mapper.map(booking, BookingForResponse.class);

    }

    @Override
    public BookingForResponse getByBookerService(int bookingId, int bookerId) {
        Booking booking = bookingRepoJpa.findById(bookingId).orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND, "Booking with id " + bookingId + " not exists in the DB"));

        int itemOwnerId = booking.getItem().getOwner().getId();
        int whoBooked = booking.getBooker().getId();
        if (bookerId == itemOwnerId || bookerId == whoBooked) {
            //  return bookingMapper.bookingToResponseBookingDto(booking);
            return mapper.map(booking, BookingForResponse.class);
        } else throw new NotFoundException(HttpStatus.NOT_FOUND, "Заказчик  не совпадает с собственником вещи");

    }

    @SneakyThrows
    @Override
    public List<BookingForResponse> getAllForBookerService(String state, int userId) {
        User booker
                = userRepoJpa.findById(userId)
                .orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND, "User with id " + userId + " not exists in the DB"));
        List<Booking> result = new ArrayList<>();
        if (state.equals(State.ALL.toString())) {
            //  result = bookingRepoJpa.getAllForUser(booker);
            result = bookingRepoJpa.findAllByBookerOrderByStartDesc(booker);
        } else if (state.equals(State.FUTURE.toString())) {
            result = bookingRepoJpa.findAllByBookerAndStartIsAfterOrderByStartDesc(
                    booker, LocalDateTime.now());
        } else if (state.equals(State.WAITING.toString())) {
            result = bookingRepoJpa.findAllByBookerAndStatusEqualsOrderByStartDesc(
                    booker, Status.WAITING);
        } else if (state.equals(State.REJECTED.toString())) {
            result = bookingRepoJpa.findAllByBookerAndStatusEqualsOrderByStartDesc(
                    booker, Status.REJECTED);
        } else if (state.equals(State.CURRENT.toString())) {
            result = bookingRepoJpa.findAllBookingsForBookerWithStartAndEnd(
                    booker, LocalDateTime.now(), LocalDateTime.now());

        } else if (state.equals(State.PAST.toString())) {
            result = bookingRepoJpa.findAllByBookerAndEndIsBeforeOrderByStartDesc(
                    booker, LocalDateTime.now());
        } else throw new UnsupportedStatusException("Unknown state: UNSUPPORTED_STATUS");
        List<BookingForResponse> bookingsListForResponse = new ArrayList<>();

        for (Booking booking : result) {
            //  BookingForResponse bookingForResponse = bookingMapper.bookingToResponseBookingDto(booking);
            BookingForResponse bookingForResponse = mapper.map(booking, BookingForResponse.class);
            bookingsListForResponse.add(bookingForResponse);
        }

        return bookingsListForResponse;
    }

    @Override
    public List<BookingForResponse> getAllForOwnerService(String state, int userId) {
        User owner
                = userRepoJpa.findById(userId)
                .orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND, "User with id " + userId + " not exists in the DB"));
        List<Booking> result = new ArrayList<>();
        if (state.equals(State.ALL.toString())) {
            result = bookingRepoJpa.getAllForOwner(owner.getId());
        } else if (state.equals(State.FUTURE.toString())) {
            result = bookingRepoJpa.findAllByOwnerAndStartIsAfterOrderByStartDesc(
                    owner.getId(), LocalDateTime.now());
        } else if (state.equals(State.WAITING.toString())) {
            result = bookingRepoJpa.findAllByOwnerAndStatusEqualsOrderByStartDesc(
                    owner.getId(), Status.WAITING);
        } else if (state.equals(State.REJECTED.toString())) {
            result = bookingRepoJpa.findAllByOwnerAndStatusEqualsOrderByStartDesc(
                    owner.getId(), Status.REJECTED);
        } else if (state.equals(State.CURRENT.toString())) {
            result = bookingRepoJpa.findAllBookingsForOwnerWithStartAndEnd(
                    owner, LocalDateTime.now(), LocalDateTime.now());
        } else if (state.equals(State.PAST.toString())) {
            result = bookingRepoJpa.findAllByOwnerAndEndIsBeforeOrderByStartDesc(
                    owner, LocalDateTime.now());
        } else throw new UnsupportedStatusException("Unknown state: UNSUPPORTED_STATUS");
        List<BookingForResponse> bookingsForResponse = new ArrayList<>();

        for (Booking booking : result) {
            //  BookingForResponse bookingForResponse = bookingMapper.bookingToResponseBookingDto(booking);
            BookingForResponse bookingForResponse = mapper.map(booking, BookingForResponse.class);
            bookingsForResponse.add(bookingForResponse);
        }

        return bookingsForResponse;
    }


}
