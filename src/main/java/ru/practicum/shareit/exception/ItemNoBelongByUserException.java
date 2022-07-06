package ru.practicum.shareit.exception;

import lombok.Getter;

@Getter
public class ItemNoBelongByUserException extends RuntimeException {

    private final String fieldName;

    public ItemNoBelongByUserException(String message) {
        super(message);
        fieldName = "User and Item";
    }
}
