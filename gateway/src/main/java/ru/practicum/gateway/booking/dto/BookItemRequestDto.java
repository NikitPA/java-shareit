package ru.practicum.gateway.booking.dto;

import java.time.LocalDateTime;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BookItemRequestDto {

    private long itemId;

    @FutureOrPresent(groups = CreateBooking.class)
    private LocalDateTime start;

    @Future(groups = CreateBooking.class)
    private LocalDateTime end;
}
