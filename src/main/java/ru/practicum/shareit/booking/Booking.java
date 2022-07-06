package ru.practicum.shareit.booking;

import lombok.*;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * // TODO .
 */
@Data
public class Booking {

    private final long id;
    @NotNull
    private final LocalDateTime startDateTime;
    @NotNull
    private final LocalDateTime endDateTime;
    private final Item item;
    private final User booker;
    @NotNull
    private final boolean status;
}
