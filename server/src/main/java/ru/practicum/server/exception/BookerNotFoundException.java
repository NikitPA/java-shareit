package ru.practicum.server.exception;

import java.text.MessageFormat;

public class BookerNotFoundException extends RuntimeException {

    public BookerNotFoundException(Long bookerId) {
        super(MessageFormat.format("Пользователь {0} не осуществлял бронирование", bookerId));
    }
}
