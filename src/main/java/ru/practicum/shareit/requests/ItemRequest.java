package ru.practicum.shareit.requests;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * // TODO .
 */
@Data
public class ItemRequest {

    private final long id;
    @NotNull
    private final String description;
    private final long requesterId;
    @NotNull
    private final LocalDateTime created;
}
