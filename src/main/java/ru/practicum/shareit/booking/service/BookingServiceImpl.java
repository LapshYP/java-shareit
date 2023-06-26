package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.mapstruct.factory.Mappers;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingForResponse;
import ru.practicum.shareit.booking.mapper.BookingMapper;
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

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepoJpa bookingRepoJpa;
    private final ItemRepoJpa itemRepoJpa;
    private final UserRepoJpa userRepoJpa;
    // private final BookingMapper bookingMapper;
    private final UserService userService;
    private final ItemService itemService;
    private final BookingMapper bookingMapper
            = Mappers.getMapper(BookingMapper.class);

    @Override
    public BookingForResponse makeBookingService(BookingDto bookingDto, int userId) {
        Item item = itemRepoJpa.findById(bookingDto.getItemId()).orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND, "Вещь не найдена"));

        if (itemRepoJpa.findById(bookingDto.getItemId()).get().getOwner().getId() == userId) {
            throw new NotFoundException(HttpStatus.NOT_FOUND, "Вещь не может быть заказана собственником этой вещи");

        }
        if (bookingDto.getStart() == null ||
                bookingDto.getEnd() == null) {
            throw new BadRequestException(HttpStatus.NOT_FOUND, "Ошибка времени создания заказа");
        }
        if (bookingDto.getStart().isAfter(bookingDto.getEnd()) ||
                bookingDto.getStart().isEqual(bookingDto.getEnd())
        ) {
            throw new BadRequestException(HttpStatus.NOT_FOUND, "Ошибка времени создания заказа");
        }

        if (bookingDto.getStart().isBefore(LocalDateTime.now().minusMinutes(5))
        ) {
            throw new BadRequestException(HttpStatus.NOT_FOUND, "Ошибка времени создания заказа");
        }
         if (item.getAvailable() == false) {
            throw new BadRequestException(HttpStatus.NOT_FOUND, "вещь не доступна");
        }

        User booker = userRepoJpa.findById(userId).orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND, "Юзер не найден"));

        Booking booking = bookingMapper.bookingDtoToBooking(bookingDto);
        booking.setBooker(booker);
        booking.setItem(item);
        booking.setStatus(Status.WAITING);

        Booking bookingToSave = bookingRepoJpa.save(booking);
        BookingForResponse bookingForResponse = bookingMapper.bookingToResponseBookingDto(bookingToSave);
        return bookingForResponse;
    }
    @Transactional
    @Override
    public BookingForResponse updateBooking(int bookingId, int userId, boolean approved) {
        Booking booking = bookingRepoJpa.findById(bookingId).orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND, "Букинг с таким айди не найден"));
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

        return bookingMapper.bookingToResponseBookingDto(bookingRepoJpa.findById(bookingId)
                .orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND, "Booking with id " + bookingId + " not exists in the DB")));

    }

    @Override
    public BookingForResponse getByBookerService(int bookingId, int bookerId) {
        Booking booking = bookingRepoJpa.findById(bookingId).orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND, "Booking with id " + bookingId + " not exists in the DB"));

        int itemOwnerId = booking.getItem().getOwner().getId();
        int whoBooked = booking.getBooker().getId();
        if (bookerId == itemOwnerId || bookerId == whoBooked) {
            return bookingMapper.bookingToResponseBookingDto(booking);
        } else throw new NotFoundException(HttpStatus.NOT_FOUND, "Заказчик  не совпадает с собственником вещи");

    }

    @SneakyThrows
    @Override
    public List<BookingForResponse> getAllForUserService(String state, int userId) {
        User userBooker = userRepoJpa.findById(userId)
                .orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND, "User with id " + userId + " not exists in the DB"));
        List<Booking> result = new ArrayList<>();
        if (state.equals(State.ALL.toString())) {
     //  result = bookingRepoJpa.getAllForUser(userBooker);
       result = bookingRepoJpa.findAllByBookerOrderByStartDesc(userBooker);
         } else if (state.equals(State.FUTURE.toString())) {
            result = bookingRepoJpa.findAllByBookerAndStartIsAfterOrderByStartDesc(
                    userBooker, LocalDateTime.now().minusHours(1));
        } else throw new UnsupportedStatusException("Unknown state: UNSUPPORTED_STATUS");
        List<BookingForResponse> bookingsForResponse = new ArrayList<>();

        for (Booking booking : result) {
            BookingForResponse bookingForResponse = bookingMapper.bookingToResponseBookingDto(booking);
            bookingsForResponse.add(bookingForResponse);
        }

        return bookingsForResponse;
    }

    @Override
    public List<BookingForResponse> getAllForOwnerService(String state, int userId) {
        User userOwner = userRepoJpa.findById(userId)
                .orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND, "User with id " + userId + " not exists in the DB"));
        List<Booking> result = new ArrayList<>();
        if (state.equals(State.ALL.toString())) {
            result = bookingRepoJpa.getAllForOwner(userOwner.getId());
        }else if (state.equals(State.FUTURE.toString())) {
            result = bookingRepoJpa.findAllByOwnerAndStartIsAfterOrderByStartDesc(
                    userOwner.getId(), LocalDateTime.now().minusHours(1));
        } else throw new UnsupportedStatusException("Unknown state: UNSUPPORTED_STATUS");
        List<BookingForResponse> bookingsForResponse = new ArrayList<>();

        for (Booking booking : result) {
            BookingForResponse bookingForResponse = bookingMapper.bookingToResponseBookingDto(booking);
            bookingsForResponse.add(bookingForResponse);
        }

        return bookingsForResponse;
    }


}
