package ru.practicum.server.request.service;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import ru.practicum.server.request.ItemRequest;
import ru.practicum.server.request.ItemRequestRepository;
import ru.practicum.server.request.ItemRequestService;
import ru.practicum.server.user.User;

import static org.mockito.Mockito.verify;

@SpringBootTest
class ItemRequestServiceTest {

    @Autowired
    private ItemRequestService requestService;

    @MockBean
    private ItemRequestRepository repository;

    @Test
    void testCreateItemRequest() {
        //Given
        ItemRequest mock = Mockito.mock(ItemRequest.class);

        //When
        requestService.createItemRequest(mock);

        //Then
        verify(repository, Mockito.times(1)).save(mock);
    }

    @Test
    void testGetAllItemRequestByUser() {
        //Given
        User user = Mockito.mock(User.class);

        //When
        requestService.getAllItemRequestByUser(user);

        //Then
        verify(repository, Mockito.times(1)).findAllByRequestorOrderByCreatedDesc(user);
    }

    @Test
    void testGetItemRequestById() {
        //Given

        //When
        requestService.getItemRequestById(1L);

        //Then
        verify(repository, Mockito.times(1)).findById(1L);
    }

    @Test
    void testGetAllItemRequest() {
        //Given
        User user = Mockito.mock(User.class);

        //When
        requestService.getAllItemRequest(user, Pageable.unpaged());

        //Then
        verify(repository, Mockito.times(1)).findAllByRequestorIsNot(user, Pageable.unpaged());
    }
}
