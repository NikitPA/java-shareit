package ru.practicum.shareit.booking.dto;

import lombok.*;
import ru.practicum.shareit.booking.Status;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * // TODO .
 */
@Data
public class BookingDto {

    @NotNull
    private final LocalDateTime startDateTime;
    @NotNull
    private final LocalDateTime endDateTime;
    private final Item item;
    private final User user;
    @NotNull
    private final Status status;


    @Data
    public static class Item {
        private final long id;
        private final String name;
    }

    @Data
    public static class User {
        private final Long id;
        private final String name;
    }
}
