package ru.practicum.server.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.server.booking.Status;

import java.time.LocalDateTime;

/**
 * // TODO .
 */
@Data
@AllArgsConstructor
public class BookingDto {

    private Long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private Item item;
    private Booker booker;
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
