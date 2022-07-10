package ru.practicum.shareit.exception;

import lombok.Getter;

@Getter
public class UserNotFoundException extends RuntimeException {

    private final String fieldName;

    public UserNotFoundException(String message) {
        super(message);
        fieldName = "User";
    }
}
