package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;

public class BookingMapper {

    public static BookingDto toBookingDto(Booking booking) {
        return new BookingDto(
                booking.getStartDateTime(),
                booking.getEndDateTime(),
                new BookingDto.Item(booking.getItem().getId(), booking.getItem().getName()),
                booking.isStatus()
        );
    }
}
