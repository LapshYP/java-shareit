package ru.practicum.shareit.booking.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingForResponse;
import ru.practicum.shareit.booking.model.Booking;

@Mapper(componentModel = "spring")
public interface BookingMapper {
    BookingDto bookingToBookingDto(Booking booking);
    Booking bookingDtoToBooking(BookingDto bookingDto);

    @Mapping(target="startTime", source = "booking.start",
            dateFormat = "dd-MM-yyyy HH:mm:ss")
    @Mapping(target="endTime", source = "booking.end",
            dateFormat = "dd-MM-yyyy HH:mm:ss")
    @Mapping(target = "status", source = "booking.status")
    BookingForResponse bookingToResponseBookingDto(Booking booking);
    Booking bookingResponseDtoToBooking(BookingForResponse bookingForResponse);

}
