package ru.practicum.shareit.exception;

import lombok.Getter;

import java.text.MessageFormat;

@Getter
public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(Long userId) {
        super(MessageFormat.format("Пользователь {0} не найден", userId));
    }
}
