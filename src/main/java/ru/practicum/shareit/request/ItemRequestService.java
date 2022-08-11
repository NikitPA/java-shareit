package ru.practicum.shareit.request;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.user.User;

import java.util.List;
import java.util.Optional;

public interface ItemRequestService {

    ItemRequest createItemRequest(ItemRequest itemRequest);

    List<ItemRequest> getAllItemRequestByUser(User user);

    Optional<ItemRequest> getItemRequestById(Long itemRequestId);

    Page<ItemRequest> getAllItemRequest(User user, Pageable pageable);
}
