package ru.practicum.server.exception;

import java.text.MessageFormat;

public class BookingNotFoundException extends RuntimeException {

    public BookingNotFoundException(Long bookingId) {
        super(MessageFormat.format("Бронирование {0} не найдено.", bookingId));
    }
}
