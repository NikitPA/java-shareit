package ru.practicum.shareit.item;

import java.util.Collection;
import java.util.Map;

public interface ItemService {

    Collection<Item> getAllItemsByUser(Long userId);

    Item getItemById(Long id);

    Item createItem(Item item);

    void deleteItem(Long id, Long userId);

    Item updateItem(Long id, Map<Object, Object> updateFields, Long userId);

    Collection<Item> findItemsByKeyword(String text);
}
