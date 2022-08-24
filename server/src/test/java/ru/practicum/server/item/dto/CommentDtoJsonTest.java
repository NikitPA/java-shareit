package ru.practicum.server.item.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.server.comment.CommentDto;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.assertj.core.api.BDDAssertions.then;

@JsonTest
class CommentDtoJsonTest {

    @Autowired
    private JacksonTester<CommentDto> json;

    @Test
    void testCreateBookingDto() throws IOException {
        //Given
        final long id = 1L;
        final String authorName = "authorName";
        final String text = "text";
        final LocalDateTime created = LocalDateTime.of(2022, 2,10,10,10,10);

        CommentDto commentDto = new CommentDto(id, authorName, text, created);

        //When
        JsonContent<CommentDto> result = json.write(commentDto);

        //Then
        then(result).extractingJsonPathNumberValue("$.id").isEqualTo((int) id);
        then(result).extractingJsonPathStringValue("$.authorName").isEqualTo(authorName);
        then(result).extractingJsonPathStringValue("$.text").isEqualTo(text);
        then(result).extractingJsonPathStringValue("$.created").isEqualTo(created.toString());
    }
}
