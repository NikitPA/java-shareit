package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

/**
 * // TODO .
 */
@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull(message = "Имя не может быть пустым.")
    private String name;

    @Email(message = "Адрес электронной почты должен быть соответствующего формата.")
    @NotNull(message = "Адрес электронной почты не может быть пустым.")
    @Column(unique = true)
    private String email;
}
