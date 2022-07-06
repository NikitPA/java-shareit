package ru.practicum.shareit.exception;

import lombok.Getter;

@Getter
public class DuplicationEmailException extends RuntimeException {

    private final String fieldName;

    public DuplicationEmailException(String message) {
        super(message);
        fieldName = "email";
    }
}
