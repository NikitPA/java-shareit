package ru.practicum.server.request;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.server.user.User;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {

    private final ItemRequestRepository itemRequestRepository;

    @Override
    public ItemRequest createItemRequest(ItemRequest itemRequest) {
        return itemRequestRepository.save(itemRequest);
    }

    @Override
    public List<ItemRequest> getAllItemRequestByUser(User user) {
        return itemRequestRepository.findAllByRequestorOrderByCreatedDesc(user);
    }

    @Override
    public Optional<ItemRequest> getItemRequestById(Long itemRequestId) {
        return itemRequestRepository.findById(itemRequestId);
    }

    @Override
    public Page<ItemRequest> getAllItemRequest(User user, Pageable pageable) {
        return itemRequestRepository.findAllByRequestorIsNot(user, pageable);
    }
}
