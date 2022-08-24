package ru.practicum.server.booking.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.assertj.core.api.BDDAssertions.then;

@JsonTest
class CreateBookingDtoJsonTest {

    @Autowired
    private JacksonTester<CreateBookingDto> json;

    @Test
    void testCreateBookingDto() throws IOException {
        //Given
        final long itemId = 1L;
        final LocalDateTime start = LocalDateTime.of(2022, 2,10,10,10,10);
        final LocalDateTime end = start.plusDays(2);

        CreateBookingDto createBookingDto = new CreateBookingDto(itemId, start, end);

        //When
        JsonContent<CreateBookingDto> result = json.write(createBookingDto);

        //Then
        then(result).extractingJsonPathNumberValue("$.itemId").isEqualTo((int) itemId);
        then(result).extractingJsonPathStringValue("$.start").isEqualTo(start.toString());
        then(result).extractingJsonPathStringValue("$.end").isEqualTo(end.toString());
    }
}
