package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.CreateItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.User;

import java.util.stream.Collectors;

public class ItemRequestMapper {

    public static ItemRequest toItemRequest(CreateItemRequestDto createItemRequestDto, User user) {
        return new ItemRequest(
                null,
                createItemRequestDto.getDescription(),
                createItemRequestDto.getCreated(),
                user
        );
    }

    public static ItemRequestDto toItemRequestDto(ItemRequest itemRequest) {
        return new ItemRequestDto(
                itemRequest.getId(),
                itemRequest.getDescription(),
                itemRequest.getCreated(),
                itemRequest.getItems().stream()
                        .map(item -> new ItemRequestDto.Item(
                                item.getId(),
                                item.getName(),
                                item.getDescription(),
                                item.getAvailable(),
                                item.getRequest().getId()
                                ))
                        .collect(Collectors.toList())
        );
    }
}
