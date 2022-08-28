package ru.practicum.server.item.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.server.exception.ItemNoBelongByUserException;
import ru.practicum.server.exception.ItemNotFoundException;
import ru.practicum.server.exception.UserNotFoundException;
import ru.practicum.server.item.Item;
import ru.practicum.server.item.ItemMapper;
import ru.practicum.server.item.ItemRepository;
import ru.practicum.server.item.ItemService;
import ru.practicum.server.user.User;
import ru.practicum.server.user.UserService;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserService userService;

    @Override
    public List<Item> getAllItemsByUser(Long userId, Pageable pageable) {
        return itemRepository.findAllByOwnerEquals(userService.getUserById(userId).orElseThrow(
                        () -> new UserNotFoundException(userId)
                ), pageable);
    }

    @Override
    public Optional<Item> getItemById(Long id) {
        return itemRepository.findById(id);
    }

    @Override
    public Item createItem(Item item) {
        return itemRepository.save(item);
    }

    @Override
    public void deleteItem(Long itemId, Long userId) {
        User userById = userService.getUserById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        Item itemById = itemRepository.findById(itemId).orElseThrow(() -> new ItemNotFoundException(itemId));
        validate(itemById.getOwner().getId(), userById.getId());
        itemRepository.delete(itemById);
    }

    @Override
    public Item updateItem(Long itemId, Map<Object, Object> updateFields, Long userId) {
        User userById = userService.getUserById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        Item itemById = itemRepository.findById(itemId).orElseThrow(() -> new ItemNotFoundException(itemId));
        validate(itemById.getOwner().getId(), userById.getId());
        Item item = ItemMapper.patchUser(itemById, updateFields);
        return itemRepository.save(item);
    }

    @Override
    public List<Item> findItemsByKeyword(String text, Pageable pageable) {
        if (text.isBlank()) {
            return Collections.emptyList();
        }
        return itemRepository.findAllByText(text, pageable);
    }

    private void validate(Long itemId, Long userId) {
        if (!Objects.equals(itemId, userId)) {
            throw new ItemNoBelongByUserException(itemId, userId);
        }
    }
}
