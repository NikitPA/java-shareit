package ru.practicum.shareit.user.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

/**
 * // TODO .
 */

@Data
public class UserDto {

    private final long id;

    @NotNull(message = "Имя не может быть пустым.")
    private final String name;

    @Email(message = "Адрес электронной почты должен быть соответствующего формата.")
    @NotNull(message = "Адрес электронной почты не может быть пустым.")
    private final String email;

}
