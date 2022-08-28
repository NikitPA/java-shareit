package ru.practicum.server.exception;

import java.text.MessageFormat;

public class ItemNotAvailableException extends RuntimeException {

    public ItemNotAvailableException(Long itemId) {
        super(MessageFormat.format("Вещь {0} не доступна для бронирования", itemId));
    }
}
