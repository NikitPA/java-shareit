package ru.practicum.gateway.user.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
public class UserDto {

    private final Long id;

    @NotNull(message = "Имя не может быть пустым.")
    private final String name;

    @Email(message = "Адрес электронной почты должен быть соответствующего формата.")
    @NotNull(message = "Адрес электронной почты не может быть пустым.")
    private final String email;

}
