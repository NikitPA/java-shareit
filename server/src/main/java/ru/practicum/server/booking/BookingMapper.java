package ru.practicum.server.booking;

import ru.practicum.server.booking.dto.BookingDto;
import ru.practicum.server.booking.dto.CreateBookingDto;
import ru.practicum.server.item.Item;
import ru.practicum.server.user.User;

public class BookingMapper {

    public static Booking toBooking(CreateBookingDto createBookingDto, User user, Item item, Status status) {
        return new Booking(null,
                createBookingDto.getStart(),
                createBookingDto.getEnd(),
                item,
                user,
                status);
    }

    public static BookingDto toBookingDto(Booking booking) {
        return new BookingDto(booking.getId(),
                booking.getStartDateTime(),
                booking.getEndDateTime(),
                new BookingDto.Item(booking.getItem().getId(), booking.getItem().getName()),
                new BookingDto.Booker(booking.getBooker().getId(), booking.getBooker().getName()),
                booking.getStatus());
    }
}
