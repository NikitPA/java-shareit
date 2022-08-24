package ru.practicum.server.exception;

import java.text.MessageFormat;

public class ItemRequestNotFoundException extends RuntimeException {

    public ItemRequestNotFoundException(Long requestId) {
        super(MessageFormat.format("Запрос на вещь {0} не найден.", requestId));
    }
}
