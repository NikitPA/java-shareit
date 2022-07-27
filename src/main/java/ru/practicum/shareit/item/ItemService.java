package ru.practicum.shareit.item;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ItemService {

    List<Item> getAllItemsByUser(Long userId);

    Optional<Item> getItemById(Long id);

    Item createItem(Item item);

    void deleteItem(Long id, Long userId);

    Item updateItem(Long id, Map<Object, Object> updateFields, Long userId);

    List<Item> findItemsByKeyword(String text);
}
