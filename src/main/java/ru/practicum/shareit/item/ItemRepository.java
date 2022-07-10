package ru.practicum.shareit.item;

import java.util.Collection;
import java.util.Optional;

public interface ItemRepository {

    Collection<Item> getAll(Long userId);

    Optional<Item> get(Long id);

    Item save(Item item);

    void delete(Long id, Long user);

    Collection<Item> search(String text);
}
