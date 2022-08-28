package ru.practicum.server.request;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.server.exception.ItemRequestNotFoundException;
import ru.practicum.server.exception.UserNotFoundException;
import ru.practicum.server.request.dto.CreateItemRequestDto;
import ru.practicum.server.request.dto.ItemRequestDto;
import ru.practicum.server.user.User;
import ru.practicum.server.user.UserService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * // TODO .
 */
@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {

    private final ItemRequestService itemRequestService;
    private final UserService userService;
    private final String header = "X-Sharer-User-Id";

    @PostMapping
    public ResponseEntity<ItemRequestDto> createItemRequest(@RequestHeader(header) Long userId,
                                                            @RequestBody CreateItemRequestDto createItemRequestDto) {
        User user = userService.getUserById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        ItemRequest itemRequest = itemRequestService
                .createItemRequest(ItemRequestMapper.toItemRequest(createItemRequestDto, user));
        return new ResponseEntity<>(ItemRequestMapper.toItemRequestDto(itemRequest), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<ItemRequestDto>> getAllItemRequestByUser(@RequestHeader(header) Long userId) {
        User user = userService.getUserById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        List<ItemRequest> allItemRequest = itemRequestService.getAllItemRequestByUser(user);
        List<ItemRequestDto> itemRequestDto = allItemRequest.stream()
                .map(ItemRequestMapper::toItemRequestDto)
                .collect(Collectors.toList());
        return new ResponseEntity<>(itemRequestDto, HttpStatus.OK);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<ItemRequestDto> getItemRequest(@RequestHeader(header) Long userId,
                                                         @PathVariable Long requestId) {
        userService.getUserById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        ItemRequest itemRequest = itemRequestService.getItemRequestById(requestId)
                .orElseThrow(() -> new ItemRequestNotFoundException(requestId));
        return new ResponseEntity<>(ItemRequestMapper.toItemRequestDto(itemRequest), HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<ItemRequestDto>> getAllItemRequest(
            @RequestHeader(header) Long userId,
            @RequestParam(name = "from", defaultValue = "0") int from,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        User user = userService.getUserById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        Page<ItemRequest> allItemRequest = itemRequestService
                .getAllItemRequest(user, PageRequest.of(from, size, Sort.by("created").descending()));
        List<ItemRequestDto> itemRequestDto = allItemRequest.stream()
                .map(ItemRequestMapper::toItemRequestDto)
                .collect(Collectors.toList());
        return new ResponseEntity<>(itemRequestDto, HttpStatus.OK);
    }
}
