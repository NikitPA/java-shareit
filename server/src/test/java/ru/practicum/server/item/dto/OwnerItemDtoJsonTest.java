package ru.practicum.server.item.dto;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.server.comment.CommentDto;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.BDDAssertions.then;

@JsonTest
class OwnerItemDtoJsonTest {

    @Autowired
    private JacksonTester<OwnerItemDto> json;

    @Test
    void testOwnerItemDto() throws IOException {
        //Given
        final long id = 1L;
        final String name = "name";
        final String description = "description";
        final boolean available = true;
        final long requestId = 2L;
        final long commentId = 4L;
        final String authorName = "authorName";
        final String text = "text";
        final LocalDateTime created = LocalDateTime.of(2022, 2,10,10,10,10);
        final long bookingIdLast = 5L;
        final long bookerIdLast = 7L;
        final long bookingIdNext = 8L;
        final long bookerIdNext = 10L;


        OwnerItemDto ownerItemDto = new OwnerItemDto(
                id, name, description, available, requestId,
                List.of(new CommentDto(commentId, authorName, text, created)),
                new OwnerItemDto.Booking(bookingIdLast, bookerIdLast),
                new OwnerItemDto.Booking(bookingIdNext, bookerIdNext)
        );

        //When
        JsonContent<OwnerItemDto> result = json.write(ownerItemDto);

        //Then
        then(result).extractingJsonPathNumberValue("$.id").isEqualTo((int) id);
        then(result).extractingJsonPathStringValue("$.name").isEqualTo(name);
        then(result).extractingJsonPathStringValue("$.description").isEqualTo(description);
        then(result).extractingJsonPathBooleanValue("$.available").isTrue();
        then(result).extractingJsonPathNumberValue("$.requestId").isEqualTo((int) requestId);
        then(result).hasJsonPathArrayValue("$.comments");
        then(result).extractingJsonPathNumberValue("$.comments[0].id").isEqualTo((int) commentId);
        then(result).extractingJsonPathStringValue("$.comments[0].authorName").isEqualTo(authorName);
        then(result).extractingJsonPathStringValue("$.comments[0].text").isEqualTo(text);
        then(result).extractingJsonPathStringValue("$.comments[0].created").isEqualTo(created.toString());
        then(result).extractingJsonPathNumberValue("$.lastBooking.id").isEqualTo((int) bookingIdLast);
        then(result).extractingJsonPathNumberValue("$.lastBooking.bookerId").isEqualTo((int) bookerIdLast);
        then(result).extractingJsonPathNumberValue("$.nextBooking.id").isEqualTo((int) bookingIdNext);
        then(result).extractingJsonPathNumberValue("$.nextBooking.bookerId").isEqualTo((int) bookerIdNext);
    }
}
