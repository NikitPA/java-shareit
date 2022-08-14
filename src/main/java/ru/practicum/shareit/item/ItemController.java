package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.comment.*;
import ru.practicum.shareit.exception.ItemNotFoundException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.OwnerItemDto;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.ItemRequestService;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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
    private final BookingService bookingService;
    private final CommentService commentService;
    private final ItemRequestService itemRequestService;
    private final String header = "X-Sharer-User-Id";
    private final String pathVariable = "itemId";

    @GetMapping
    public ResponseEntity<Collection<OwnerItemDto>> getItems(
            @RequestHeader(header) Long userId,
            @RequestParam(name = "from", defaultValue = "0") @Min(0) int from,
            @RequestParam(name = "size", defaultValue = "10") @Min(1) int size
    ) {
        userService.getUserById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        Collection<Item> allItemsByUser = itemService
                .getAllItemsByUser(userId, PageRequest.of(from, size, Sort.by("id").ascending()));
        List<OwnerItemDto> allItemDtoByUser = allItemsByUser.stream()
                .map(item -> ItemMapper.toOwnerItemDto(
                        item, bookingService.getNextBookingOfItem(item),
                        bookingService.getLastBookingOfItem(item)))
                .collect(Collectors.toList());
        return new ResponseEntity<>(allItemDtoByUser, HttpStatus.OK);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<OwnerItemDto> getItemById(@PathVariable(pathVariable) @Min(1) long itemId,
                                                    @RequestHeader(header) Long userId) {
        userService.getUserById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        Item itemById = itemService.getItemById(itemId).orElseThrow(() -> new ItemNotFoundException(itemId));
        if (Objects.equals(userId, itemById.getOwner().getId())) {
            return new ResponseEntity<>(ItemMapper.toOwnerItemDto(
                    itemById, bookingService.getNextBookingOfItem(itemById),
                    bookingService.getLastBookingOfItem(itemById)), HttpStatus.OK);
        }
        return new ResponseEntity<>(ItemMapper.toOwnerItemDto(
                itemById, null, null), HttpStatus.OK
        );
    }

    @PostMapping
    public ResponseEntity<ItemDto> createItem(@Valid @RequestBody ItemDto itemDto,
                                              @RequestHeader(header) Long userId) {
        User userById = userService.getUserById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        ItemRequest itemRequest = itemDto.getRequestId() != null ?
                itemRequestService.getItemRequestById(itemDto.getRequestId()).orElse(null) : null;
        Item item = itemService
                .createItem(ItemMapper.toItem(itemDto, userById, itemRequest));
        return new ResponseEntity<>(ItemMapper.toItemDto(item), HttpStatus.CREATED);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<ItemDto> updateItem(@PathVariable(pathVariable) @Min(1) long itemId,
                                              @RequestBody Map<Object, Object> updateFields,
                                              @RequestHeader(header) Long userId) {
        Item item = itemService.updateItem(itemId, updateFields, userId);
        return new ResponseEntity<>(ItemMapper.toItemDto(item), HttpStatus.OK);
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<String> deleteItem(@PathVariable(pathVariable) long itemId,
                                             @RequestHeader(header) Long userId) {
        itemService.deleteItem(itemId, userId);
        return new ResponseEntity<>("valid", HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<Collection<ItemDto>> searchAvailableItems(
            @RequestParam("text") String text,
            @RequestParam(name = "from", defaultValue = "0") @Min(0) int from,
            @RequestParam(name = "size", defaultValue = "10") @Min(1) int size
    ) {
        Collection<Item> itemsByKeyword = itemService.findItemsByKeyword(text, PageRequest.of(from, size));
        List<ItemDto> itemDtoByKeyword = itemsByKeyword
                .stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
        return new ResponseEntity<>(itemDtoByKeyword, HttpStatus.OK);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<CommentDto> createComment(@Valid @RequestBody CreateCommentDto createCommentDto,
                                                    @RequestHeader(header) Long userId,
                                                    @PathVariable(pathVariable) long itemId) {
        User userById = userService.getUserById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        Item item = itemService.getItemById(itemId).orElseThrow(() -> new ItemNotFoundException(itemId));
        Comment comment = commentService.createComment(
                CommentMapper.toComment(createCommentDto, userById, item), userById, item
        );
        return new ResponseEntity<>(CommentMapper.toCommentDto(comment), HttpStatus.OK);
    }
}
