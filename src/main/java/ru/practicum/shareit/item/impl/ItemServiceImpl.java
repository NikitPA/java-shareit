package ru.practicum.shareit.item.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ItemNoBelongByUserException;
import ru.practicum.shareit.exception.ItemNotFoundException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserService;

import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserService userService;

    @Override
    public List<Item> getAllItemsByUser(@NotNull Long userId) {
        return itemRepository.findAllByOwnerEquals(userService.getUserById(userId).orElseThrow(
                        () -> new UserNotFoundException(userId)
                ))
                .stream()
                .filter(item -> item.getOwner().getId() == userId)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Item> getItemById(@NotNull Long id) {
        return itemRepository.findById(id);
    }

    @Override
    public Item createItem(@NotNull Item item) {
        return itemRepository.save(item);
    }

    @Override
    public void deleteItem(@NotNull Long itemId, @NotNull Long userId) {
        User userById = userService.getUserById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        Item itemById = getItemById(itemId).orElseThrow(() -> new ItemNotFoundException(itemId));
        validate(itemById.getOwner().getId(), userById.getId());
        itemRepository.delete(itemById);
    }

    @Override
    public Item updateItem(@NotNull Long itemId, @NotNull Map<Object, Object> updateFields, Long userId) {
        User userById = userService.getUserById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        Item itemById = getItemById(itemId).orElseThrow(() -> new ItemNotFoundException(itemId));
        validate(itemById.getOwner().getId(), userById.getId());
        Item item = ItemMapper.patchUser(itemById, updateFields);
        return itemRepository.save(item);
    }

    @Override
    public List<Item> findItemsByKeyword(@NotNull String text) {
        if (text.isBlank()) {
            return Collections.emptyList();
        }
        return itemRepository.findAllByText(text);
    }

    private void validate(Long itemId, Long userId) {
        if (!Objects.equals(itemId, userId)) {
            throw new ItemNoBelongByUserException(itemId, userId);
        }
    }
}
