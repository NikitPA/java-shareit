package ru.practicum.shareit.booking.dto;

import lombok.*;

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
    @NotNull
    private final boolean status;


    @Data
    public static class Item {
        private final long id;
        private final String name;
    }
}
