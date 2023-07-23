package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.exception.BadRequestException;

import java.time.LocalDateTime;
import java.util.Map;

@Service
public class BookingClient extends BaseClient {
    private static final String API_PREFIX = "/bookings";

    @Autowired
    public BookingClient(@Value("${shareit.server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(builder.uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX)).requestFactory(HttpComponentsClientHttpRequestFactory::new).build());
    }

    public ResponseEntity<Object> getAllForBookerService(BookingState state, int userId, int from, int size) {
        Map<String, Object> parameters = Map.of("state", state.name(), "from", from, "size", size);
        return get("?state={state}&from={from}&size={size}", userId, parameters);
    }

    public ResponseEntity<Object> getAllForOwnerService(BookingState state, int userId, int from, int size) {
        Map<String, Object> parameters = Map.of("state", state.name(), "from", from, "size", size);
        return get("/owner?state={state}&from={from}&size={size}", userId, parameters);
    }


    public ResponseEntity<Object> makeBookingService(BookingDto bookingDto, int userId) {
        if (bookingDto.getStart().isAfter(bookingDto.getEnd()) ||
                bookingDto.getStart().isEqual(bookingDto.getEnd())
        ) {
            throw new BadRequestException(HttpStatus.NOT_FOUND, "Ошибка времени создания букинга");
        }

        if (bookingDto.getStart().isBefore(LocalDateTime.now())
        ) {
            throw new BadRequestException(HttpStatus.NOT_FOUND, "Ошибка времени создания букинга");
        }


        return post("", userId, bookingDto);
    }

    public ResponseEntity<Object> updateBooking(int bookingId, int userId, boolean approved) {
        return patch("/" + bookingId + "?approved=" + approved, userId);

    }

    public ResponseEntity<Object> getByBookerService(int bookingId, int userId) {
        return get("/" + bookingId, userId);
    }
}