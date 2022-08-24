package ru.practicum.server.user.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.io.IOException;

import static org.assertj.core.api.BDDAssertions.then;

@JsonTest
class UserDtoJsonTest {

    @Autowired
    private JacksonTester<UserDto> json;

    @Test
    void testUserDto() throws IOException {
        // Given
        final long id = 1L;
        final String name = "Nikita";
        final String email = "email@yandex.com";

        UserDto userDto = new UserDto(id, name, email);

        // When
        JsonContent<UserDto> result = json.write(userDto);

        // Then
        then(result).extractingJsonPathNumberValue("$.id").isEqualTo((int) id);
        then(result).extractingJsonPathStringValue("$.name").isEqualTo(name);
        then(result).extractingJsonPathStringValue("$.email").isEqualTo(email);
    }
}
