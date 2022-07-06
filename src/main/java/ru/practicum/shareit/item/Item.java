package ru.practicum.shareit.item;

import lombok.*;
import ru.practicum.shareit.requests.ItemRequest;
import ru.practicum.shareit.user.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * // TODO .
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
public class Item {

    @EqualsAndHashCode.Include
    private Long id;
    @NotBlank(message = "Имя не может быть пустым.")
    @NotNull(message = "Имя должно передаваться параметром.")
    private String name;
    @NotBlank(message = "Описание не может быть пустым.")
    @NotNull(message = "Описание должно передаваться параметром.")
    private String description;
    @NotNull(message = "Нужно указать доступна вещь или нет.")
    private Boolean available;
    @NotNull
    @EqualsAndHashCode.Include
    private User owner;
    private ItemRequest request;
}
