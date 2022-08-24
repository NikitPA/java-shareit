package ru.practicum.server.exception;

import java.text.MessageFormat;

public class ItemNotFoundException extends RuntimeException {
    public ItemNotFoundException(Long itemId) {
        super(MessageFormat.format("Товар {0} не найден.", itemId));
    }
}
