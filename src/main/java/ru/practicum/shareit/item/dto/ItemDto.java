package ru.practicum.shareit.item.dto;


import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * // TODO .
 */
@Data
public class ItemDto {

    private final Long id;
    @NotBlank(message = "Имя не может быть пустым.")
    @NotNull(message = "Имя должно передаваться параметром.")
    private final String name;
    @NotBlank(message = "Описание не может быть пустым.")
    @NotNull(message = "Описание должно передаваться параметром.")
    private final String description;
    @NotNull(message = "Нужно указать доступна вещь или нет.")
    private final Boolean available;
    private final Long requestId;
}
