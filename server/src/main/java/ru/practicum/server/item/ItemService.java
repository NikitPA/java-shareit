package ru.practicum.server.item;

import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ItemService {

    List<Item> getAllItemsByUser(Long userId, Pageable pageable);

    Optional<Item> getItemById(Long id);

    Item createItem(Item item);

    void deleteItem(Long id, Long userId);

    Item updateItem(Long itemId, Map<Object, Object> updateFields, Long userId);

    List<Item> findItemsByKeyword(String text, Pageable pageable);
}
