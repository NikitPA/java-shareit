package ru.practicum.server.item.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.server.comment.CommentDto;

import java.util.Collection;

/**
 * // TODO .
 */
@Data
@AllArgsConstructor
public class ItemDto {

    private final Long id;
    private String name;
    private String description;
    private Boolean available;
    private Long requestId;
    private final Collection<CommentDto> comments;
}
