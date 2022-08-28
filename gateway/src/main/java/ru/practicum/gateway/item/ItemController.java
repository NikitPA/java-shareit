package ru.practicum.gateway.item;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.gateway.item.dto.CreateCommentDto;
import ru.practicum.gateway.item.dto.ItemDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Map;

@RestController
@RequestMapping("/items")
@Validated
@RequiredArgsConstructor
public class ItemController {

    private final ItemClient itemClient;
    private final String header = "X-Sharer-User-Id";
    private final String pathVariable = "itemId";

    @GetMapping
    public ResponseEntity<Object> getItems(
            @RequestHeader(header) Long userId,
            @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero int from,
            @RequestParam(name = "size", defaultValue = "10") @Positive int size
    ) {
        return itemClient.getItems(userId, from, size);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItemById(@PathVariable(pathVariable) long itemId,
                                                    @RequestHeader(header) Long userId) {
        return itemClient.getItemById(itemId, userId);
    }

    @PostMapping
    public ResponseEntity<Object> createItem(@Valid @RequestBody ItemDto itemDto,
                                              @RequestHeader(header) Long userId) {
        return itemClient.createItem(itemDto, userId);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(@PathVariable(pathVariable) long itemId,
                                              @RequestBody Map<Object, Object> updateFields,
                                              @RequestHeader(header) Long userId) {
        return itemClient.updateItem(itemId, userId, updateFields);
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<Object> deleteItem(@PathVariable(pathVariable) long itemId,
                                             @RequestHeader(header) Long userId) {
        return itemClient.deleteUser(itemId, userId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchAvailableItems(
            @RequestParam("text") String text,
            @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero int from,
            @RequestParam(name = "size", defaultValue = "10") @Positive int size
    ) {
        return itemClient.searchItem(text, from, size);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> createComment(@Valid @RequestBody CreateCommentDto createCommentDto,
                                                    @RequestHeader(header) Long userId,
                                                    @PathVariable(pathVariable) long itemId) {
        return itemClient.createComment(createCommentDto, userId, itemId);
    }
}
