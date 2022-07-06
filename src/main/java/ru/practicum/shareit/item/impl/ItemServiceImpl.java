package ru.practicum.shareit.item.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ItemNoBelongByUserException;
import ru.practicum.shareit.exception.ItemNotFoundException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserService;

import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserService userService;

    @Override
    public Collection<Item> getAllItemsByUser(@NotNull Long userId) {
        return itemRepository.getAll(userId);
    }

    @Override
    public Item getItemById(@NotNull Long id) {
        return itemRepository.get(id).orElseThrow(
                () -> new ItemNotFoundException(String.format("Товар %d не найден.", id)));
    }

    @Override
    public Item createItem(@NotNull Item item) {
        return itemRepository.save(item);
    }

    @Override
    public void deleteItem(@NotNull Long id, @NotNull Long userId) {
        User userById = userService.getUserById(userId);
        Item itemById = getItemById(id);
        validate(itemById.getOwner().getId(), userById.getId());
        itemRepository.delete(id, userId);
    }

    @Override
    public Item updateItem(@NotNull Long id, @NotNull Map<Object, Object> updateFields, Long userId) {
        User userById = userService.getUserById(userId);
        Item itemById = getItemById(id);
        validate(itemById.getOwner().getId(), userById.getId());
        Item item = ItemMapper.patchUser(itemById, updateFields);
        return itemRepository.save(item);
    }

    @Override
    public Collection<Item> findItemsByKeyword(@NotNull String text) {
        if (text.isBlank()) {
            return Collections.emptyList();
        }
        return itemRepository.search(text);
    }

    private void validate(Long id, Long userId) {
        if (!Objects.equals(id, userId)) {
            throw new ItemNoBelongByUserException(
                    String.format("Вещь %d не принадлежит пользователю %d", id, userId));
        }
    }
}
