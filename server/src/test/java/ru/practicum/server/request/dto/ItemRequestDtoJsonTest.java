package ru.practicum.server.request.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.BDDAssertions.then;

@JsonTest
class ItemRequestDtoJsonTest {

    @Autowired
    private JacksonTester<ItemRequestDto> json;

    @Test
    void testItemRequestDto() throws IOException {
        //Given
        final long requestId = 1L;
        final String requestDescription = "description";
        final LocalDateTime created = LocalDateTime.of(2022, 2,10,10,10,10);
        final long itemId = 2L;
        final String name = "name";
        final String itemDescription = "descriptionItem";
        final boolean available = true;


        ItemRequestDto itemRequestDto = new ItemRequestDto(
                requestId, requestDescription, created, List.of(new ItemRequestDto.Item(
                        itemId, name, itemDescription, available, requestId
        ))
        );

        //When
        JsonContent<ItemRequestDto> result = json.write(itemRequestDto);

        //Then
        then(result).extractingJsonPathNumberValue("$.id").isEqualTo((int) requestId);
        then(result).extractingJsonPathStringValue("$.description").isEqualTo(requestDescription);
        then(result).extractingJsonPathStringValue("$.created").isEqualTo(created.toString());
        then(result).hasJsonPathArrayValue("$.items");
        then(result).extractingJsonPathNumberValue("$.items[0].id").isEqualTo((int) itemId);
        then(result).extractingJsonPathStringValue("$.items[0].name").isEqualTo(name);
        then(result).extractingJsonPathStringValue("$.items[0].description").isEqualTo(itemDescription);
        then(result).extractingJsonPathBooleanValue("$.items[0].available").isTrue();
        then(result).extractingJsonPathNumberValue("$.items[0].requestId").isEqualTo((int) requestId);
    }
}
