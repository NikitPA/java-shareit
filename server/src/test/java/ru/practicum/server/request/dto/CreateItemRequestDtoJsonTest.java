package ru.practicum.server.request.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.io.IOException;

import static org.assertj.core.api.BDDAssertions.then;

@JsonTest
class CreateItemRequestDtoJsonTest {

    @Autowired
    private JacksonTester<CreateItemRequestDto> json;

    @Test
    void testUserDto() throws IOException {
        // Given
        final String description = "description";

        CreateItemRequestDto createItemRequestDto = new CreateItemRequestDto(description);

        // When
        JsonContent<CreateItemRequestDto> result = json.write(createItemRequestDto);

        // Then
        then(result).extractingJsonPathStringValue("$.description").isEqualTo(description);
    }
}