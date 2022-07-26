package ru.practicum.shareit.booking.dto;

import lombok.Data;
import ru.practicum.shareit.booking.CreateBooking;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class CreateBookingDto {

    @NotNull
    private Long itemId;

    @NotNull
    @Future(groups = CreateBooking.class)
    private LocalDateTime start;

    @NotNull
    @Future(groups = CreateBooking.class)
    private LocalDateTime end;
}
