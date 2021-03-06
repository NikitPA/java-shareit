package ru.practicum.shareit.user;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

/**
 * // TODO .
 */
@Data
@AllArgsConstructor
public class User {

    private long id;
    @NotNull(message = "Имя не может быть пустым.")
    private String name;
    @Email(message = "Адрес электронной почты должен быть соответствующего формата.")
    @NotNull(message = "Адрес электронной почты не может быть пустым.")
    private String email;
}
