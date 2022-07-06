package ru.practicum.shareit.item.impl;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Repository
public class InMemoryItemRepository implements ItemRepository {

    private Map<Long, List<Item>> items = new HashMap<>();
    private long count = 1;

    @Override
    public Collection<Item> getAll(Long userId) {
        return items.get(userId);
    }

    @Override
    public Optional<Item> get(Long id) {
        return items.values()
                .stream()
                .flatMap(Collection::stream)
                .filter(item -> item.getId().equals(id)).findFirst();
    }

    @Override
    public Item save(Item itemSave) {
        if (itemSave.getId() == null) {
            itemSave.setId(count++);
        }
        items.compute(itemSave.getOwner().getId(), (userId, userItems) -> {
            if (userItems == null) {
                userItems = new ArrayList<>();
            }
            if (userItems.contains(itemSave)) {
                int index = userItems.indexOf(itemSave);
                userItems.set(index, itemSave);
                return userItems;
            }
            userItems.add(itemSave);
            return userItems;
        });
        return get(itemSave.getId()).orElseThrow();
    }

    @Override
    public void delete(Long id, Long user) {
        items.compute(user, (userId, userItems) -> {
            if (userItems == null) {
                userItems = new ArrayList<>();
                return userItems;
            }
            userItems.remove(id);
            return userItems;
        });
    }

    @Override
    public Collection<Item> search(String text) {
        return items.values()
                .stream()
                .flatMap(Collection::stream)
                .filter(item -> {
                    if (item.getAvailable()) {
                        Pattern pattern = Pattern.compile(".?" + text.toLowerCase() + ".+");
                        Matcher matcher = pattern.matcher(item.getName().toLowerCase());
                        while (matcher.find()) {
                            return true;
                        }
                        Matcher match = pattern.matcher(item.getDescription().toLowerCase());
                        while (match.find()) {
                            return true;
                        }
                    }
                    return false;
                }).collect(Collectors.toList());
    }
}
