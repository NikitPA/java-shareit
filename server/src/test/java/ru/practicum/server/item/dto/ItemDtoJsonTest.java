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
class ItemDtoJsonTest {

    @Autowired
    private JacksonTester<ItemDto> json;

    @Test
    void testItemDto() throws IOException {
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

        ItemDto itemDto = new ItemDto(
                id, name, description, available, requestId, List.of(
                        new CommentDto(commentId, authorName, text, created)
        ));

        //When
        JsonContent<ItemDto> result = json.write(itemDto);

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
    }
}
