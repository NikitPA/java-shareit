package ru.practicum.server.exception;

import java.text.MessageFormat;

public class UserNotGiveItemException extends RuntimeException {

    public UserNotGiveItemException(Long userId, Long itemId) {
        super(MessageFormat.format("Пользователь {0} не брал или не завершил заказ {1}.", userId, itemId));
    }
}
