package ru.practicum.server.exception;

import java.text.MessageFormat;

public class ItemNoBelongByUserException extends RuntimeException {


    public ItemNoBelongByUserException(Long itemId, Long userId) {
        super(MessageFormat.format("Вещь {0} не принадлежит пользователю {1}", itemId, userId));
    }
}
