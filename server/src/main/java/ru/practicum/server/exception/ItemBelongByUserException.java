package ru.practicum.server.exception;

import java.text.MessageFormat;

public class ItemBelongByUserException extends RuntimeException {


    public ItemBelongByUserException(Long itemId, Long userId) {
        super(MessageFormat.format("Вещь {0} принадлежит пользователю {1}", itemId, userId));
    }
}
