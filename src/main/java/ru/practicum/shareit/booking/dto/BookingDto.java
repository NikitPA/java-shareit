package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.booking.Status;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * // TODO .
 */
@Data
@AllArgsConstructor
public class BookingDto {

    private Long id;

    @NotNull
    private LocalDateTime startDateTime;

    @NotNull
    private LocalDateTime endDateTime;

    private Item item;

    private Booker booker;

    @NotNull
    private Status status;


    @Data
    public static class Item {
        private final long id;
        private final String name;
    }

    @Data
    public static class Booker {
        private final Long id;
        private final String name;
    }
}
