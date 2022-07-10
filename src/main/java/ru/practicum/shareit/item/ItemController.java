package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * // TODO .
 */
@RestController
@RequestMapping("/items")
@Validated
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<Collection<ItemDto>> getItems(@RequestHeader("X-Sharer-User-Id") Long userId) {
        Collection<Item> allItemsByUser = itemService.getAllItemsByUser(userId);
        List<ItemDto> allItemDtoByUser = allItemsByUser.stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
        return new ResponseEntity<>(allItemDtoByUser, HttpStatus.OK);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<ItemDto> getItemById(@PathVariable("itemId") @Min(1) long itemId) {
        Item itemById = itemService.getItemById(itemId);
        return new ResponseEntity<>(ItemMapper.toItemDto(itemById), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ItemDto> createUser(@Valid @RequestBody ItemDto itemDto,
                                              @RequestHeader("X-Sharer-User-Id") Long userId) {
        User userById = userService.getUserById(userId);
        //Добавить поиск itemRequest
        Item item = itemService.createItem(ItemMapper.toItem(itemDto, userById, null));
        return new ResponseEntity<>(ItemMapper.toItemDto(item), HttpStatus.CREATED);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<ItemDto> updateItem(@PathVariable("itemId") long itemId,
                                              @RequestBody Map<Object, Object> updateFields,
                                              @RequestHeader("X-Sharer-User-Id") Long userId) {
        Item item = itemService.updateItem(itemId, updateFields, userId);
        return new ResponseEntity<>(ItemMapper.toItemDto(item), HttpStatus.OK);
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<String> deleteUser(@PathVariable("itemId") long itemId,
                                             @RequestHeader("X-Sharer-User-Id") Long userId) {
        itemService.deleteItem(itemId, userId);
        return new ResponseEntity<>("valid", HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<Collection<ItemDto>> searchAvailableItems(@RequestParam("text") String text) {
        Collection<Item> itemsByKeyword = itemService.findItemsByKeyword(text);
        List<ItemDto> itemDtoByKeyword = itemsByKeyword.stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
        return new ResponseEntity<>(itemDtoByKeyword, HttpStatus.OK);
    }
}
