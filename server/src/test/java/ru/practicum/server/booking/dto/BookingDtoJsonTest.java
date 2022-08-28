package ru.practicum.server.booking.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.server.booking.Status;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.assertj.core.api.BDDAssertions.then;

@JsonTest
class BookingDtoJsonTest {

    @Autowired
    private JacksonTester<BookingDto> json;

    @Test
    void testBookingDto() throws IOException {
        //Given
        final long id = 1L;
        final LocalDateTime start = LocalDateTime.of(2022, 2,10,10,10,10);
        final LocalDateTime end = start.plusDays(2);
        final long idItem = 2L;
        final String nameItem = "nameItem";
        final long idBooker = 26L;
        final String nameBooker = "nameBooker";
        final Status status = Status.WAITING;


        BookingDto bookingDto = new BookingDto(
                id, start, end,
                new BookingDto.Item(idItem, nameItem),
                new BookingDto.Booker(idBooker, nameBooker),
                status
        );

        //When
        JsonContent<BookingDto> result = json.write(bookingDto);

        //Then
        then(result).extractingJsonPathNumberValue("$.id").isEqualTo((int) id);
        then(result).extractingJsonPathStringValue("$.start").isEqualTo(start.toString());
        then(result).extractingJsonPathStringValue("$.end").isEqualTo(end.toString());
        then(result).extractingJsonPathNumberValue("$.item.id").isEqualTo((int) idItem);
        then(result).extractingJsonPathStringValue("$.item.name").isEqualTo(nameItem);
        then(result).extractingJsonPathNumberValue("$.booker.id").isEqualTo((int) idBooker);
        then(result).extractingJsonPathStringValue("$.booker.name").isEqualTo(nameBooker);
        then(result).extractingJsonPathStringValue("$.status").isEqualTo(status.name());
    }
}

