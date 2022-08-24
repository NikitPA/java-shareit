package ru.practicum.server.item.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.server.comment.CreateCommentDto;

import java.io.IOException;

import static org.assertj.core.api.BDDAssertions.then;

@JsonTest
class CreateCommentDtoJsonTest {

    @Autowired
    private JacksonTester<CreateCommentDto> json;

    @Test
    void testCreateBookingDto() throws IOException {
        //Given
        final String text = "text";

        CreateCommentDto createCommentDto = new CreateCommentDto(text);

        //When
        JsonContent<CreateCommentDto> result = json.write(createCommentDto);

        //Then
        then(result).extractingJsonPathStringValue("$.text").isEqualTo(text);
    }
}
