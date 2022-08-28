package ru.practicum.gateway.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Collection;

@Data
@AllArgsConstructor
public class ItemDto {

    private final Long id;

    @NotBlank(message = "Имя не может быть пустым.")
    @NotNull(message = "Имя должно передаваться параметром.")
    private String name;

    @NotBlank(message = "Описание не может быть пустым.")
    @NotNull(message = "Описание должно передаваться параметром.")
    private String description;

    @NotNull(message = "Нужно указать доступна вещь или нет.")
    private Boolean available;

    private Long requestId;

    private final Collection<CommentDto> comments;
}
